package com.huali.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.crazycake.shiro.common.SessionInMemory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author myUser
 * @date 2021-02-07 20:49
 **/
@Slf4j
public class RedisSessionDAO extends AbstractSessionDAO {


    private static final String DEFAULT_SESSION_KEY_PREFIX = "shiro:session:";
    private String keyPrefix = DEFAULT_SESSION_KEY_PREFIX;

    private static final long DEFAULT_SESSION_IN_MEMORY_TIMEOUT = 1000L;
    /**
     * doReadSession be called about 10 times when login.
     * Save Session in ThreadLocal to resolve this problem. sessionInMemoryTimeout is expiration of Session in ThreadLocal.
     * The default value is 1000 milliseconds (1s).
     * Most of time, you don't need to change it.
     */
    private long sessionInMemoryTimeout = DEFAULT_SESSION_IN_MEMORY_TIMEOUT;

    private static final boolean DEFAULT_SESSION_IN_MEMORY_ENABLED = true;

    private boolean sessionInMemoryEnabled = DEFAULT_SESSION_IN_MEMORY_ENABLED;

    // expire time in seconds
    private static final int DEFAULT_EXPIRE = -2;
    private static final int NO_EXPIRE = -1;

    /**
     * Please make sure expire is longer than sesion.getTimeout()
     */
    private int expire = DEFAULT_EXPIRE;

    private static final int MILLISECONDS_IN_A_SECOND = 1000;

    private RedisTemplate<String, Serializable> redisManager;
    private static ThreadLocal<Map<Serializable, SessionInMemory>> sessionsInThread = new ThreadLocal<>();

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
        if (this.sessionInMemoryEnabled) {
            this.setSessionToThreadLocal(session.getId(), session);
        }
    }

    /**
     * save session
     *
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            log.error("session or session id is null");
            throw new UnknownSessionException("session or session id is null");
        }

        BoundValueOperations<String, Serializable> operations = this.redisManager.boundValueOps(getRedisSessionKey(session.getId()));
        if (expire == DEFAULT_EXPIRE) {
            operations.set((SimpleSession) session, session.getTimeout(), TimeUnit.MILLISECONDS);
            return;
        }

        if (expire != NO_EXPIRE && expire * MILLISECONDS_IN_A_SECOND < session.getTimeout()) {
            log.warn("Redis session expire time: "
                    + (expire * MILLISECONDS_IN_A_SECOND)
                    + " is less than Session timeout: "
                    + session.getTimeout()
                    + " . It may cause some problems.");
        }
        operations.set((SimpleSession) session, expire, TimeUnit.SECONDS);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            log.error("session or session id is null");
            return;
        }

        redisManager.delete(getRedisSessionKey(session.getId()));
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<>();
        Set<String> keys = redisManager.keys(this.keyPrefix + "*");
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                Session s = (Session) redisManager.boundValueOps(key).get();
                sessions.add(s);
            }
        }
        return sessions;
    }

    @Override
    protected Serializable doCreate(Session session) {
        if (session == null) {
            log.error("session is null");
            throw new UnknownSessionException("session is null");
        }
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            log.warn("session id is null");
            return null;
        }

        if (this.sessionInMemoryEnabled) {
            Session session = getSessionFromThreadLocal(sessionId);
            if (session != null) {
                return session;
            }
        }

        Session session = (Session) redisManager.boundValueOps(getRedisSessionKey(sessionId)).get();
        if (this.sessionInMemoryEnabled) {
            setSessionToThreadLocal(sessionId, session);
        }
        return session;
    }

    private void setSessionToThreadLocal(Serializable sessionId, Session s) {
        Map<Serializable, SessionInMemory> sessionMap = sessionsInThread.get();
        if (sessionMap == null) {
            sessionMap = new HashMap<>();
            sessionsInThread.set(sessionMap);
        }

        removeExpiredSessionInMemory(sessionMap);

        SessionInMemory sessionInMemory = new SessionInMemory();
        sessionInMemory.setCreateTime(new Date());
        sessionInMemory.setSession(s);
        sessionMap.put(sessionId, sessionInMemory);
    }

    private void removeExpiredSessionInMemory(Map<Serializable, SessionInMemory> sessionMap) {
        Iterator<Serializable> it = sessionMap.keySet().iterator();
        while (it.hasNext()) {
            Serializable sessionId = it.next();
            SessionInMemory sessionInMemory = sessionMap.get(sessionId);
            if (sessionInMemory == null) {
                it.remove();
                continue;
            }
            long liveTime = getSessionInMemoryLiveTime(sessionInMemory);
            if (liveTime > sessionInMemoryTimeout) {
                it.remove();
            }
        }
    }

    private Session getSessionFromThreadLocal(Serializable sessionId) {
        if (sessionsInThread.get() == null) {
            return null;
        }

        Map<Serializable, SessionInMemory> sessionMap = sessionsInThread.get();
        SessionInMemory sessionInMemory = sessionMap.get(sessionId);
        if (sessionInMemory == null) {
            return null;
        }
        long liveTime = getSessionInMemoryLiveTime(sessionInMemory);
        if (liveTime > sessionInMemoryTimeout) {
            sessionMap.remove(sessionId);
            return null;
        }

        return sessionInMemory.getSession();
    }

    private long getSessionInMemoryLiveTime(SessionInMemory sessionInMemory) {
        Date now = new Date();
        return now.getTime() - sessionInMemory.getCreateTime().getTime();
    }

    private String getRedisSessionKey(Serializable sessionId) {
        return this.keyPrefix + sessionId;
    }

    public RedisTemplate<String, Serializable> getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisTemplate<String, Serializable> redisManager) {
        this.redisManager = redisManager;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public long getSessionInMemoryTimeout() {
        return sessionInMemoryTimeout;
    }

    public void setSessionInMemoryTimeout(long sessionInMemoryTimeout) {
        this.sessionInMemoryTimeout = sessionInMemoryTimeout;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public boolean getSessionInMemoryEnabled() {
        return sessionInMemoryEnabled;
    }

    public void setSessionInMemoryEnabled(boolean sessionInMemoryEnabled) {
        this.sessionInMemoryEnabled = sessionInMemoryEnabled;
    }

    public static ThreadLocal<Map<Serializable, SessionInMemory>> getSessionsInThread() {
        return sessionsInThread;
    }
}