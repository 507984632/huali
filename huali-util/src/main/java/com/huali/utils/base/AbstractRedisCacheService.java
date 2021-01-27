package com.huali.utils.base;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.huali.utils.base.service.FieldEditLogService;
import com.huali.utils.enums.EntityCacheableStrategy;
import com.huali.utils.exception.Assert;
import com.huali.utils.exception.CheckedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.huali.utils.enums.EntityCacheableStrategy.ALWAYS;
import static com.huali.utils.enums.EntityCacheableStrategy.NON_TRANSACTIONAL;

/**
 * @author myUser
 * @date 2021-01-26 17:03
 **/
@Slf4j
public abstract class AbstractRedisCacheService<T extends BaseModel, M extends BaseMapper<T>> extends BaseService<T, M> {

    @Autowired
    private FieldEditLogService fieldEditLogService;
    @Autowired
    protected RedisTemplate<String, Serializable> redisTemplate;

    protected EntityCacheableStrategy getEntityCacheableStrategy() {
        return NON_TRANSACTIONAL;
    }

    /**
     * TimeUnit.DAYS
     */
    public int getTimeout() {
        return 15;
    }

    public T getCache(Long id) {
        String cacheKey = this.getCacheKey(id);
        BoundValueOperations<String, Serializable> operations = redisTemplate.boundValueOps(cacheKey);

        @SuppressWarnings("unchecked")
        T cacheEntity = (T) operations.get();
        if (cacheEntity == null) {
            cacheEntity = super.getById(id);
            operations.set(cacheEntity, getTimeout(), TimeUnit.DAYS);
        }
        return cacheEntity;
    }

    @Override
    public T getById(Long id) {
        String cacheKey = this.getCacheKey(id);
        BoundValueOperations<String, Serializable> operations = redisTemplate.boundValueOps(cacheKey);

        @SuppressWarnings("unchecked")
        T cacheEntity = (T) operations.get();
        if (cacheEntity == null) {
            cacheEntity = super.getById(id);

            switch (this.getEntityCacheableStrategy()) {
                case NON_TRANSACTIONAL: {
                    if (TransactionSynchronizationManager.isSynchronizationActive()) {
                        break;
                    }
                }
                case ALWAYS: {
                    operations.set(cacheEntity, getTimeout(), TimeUnit.DAYS);
                }
                default:
            }
        }
        return cacheEntity;
    }

    @Override
    @Transactional
    public T insert(T entity) {
        entity = super.insert(entity);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            String cacheKey = this.getCacheKey(entity.getId());

            EntityCacheableStrategy strategy = this.getEntityCacheableStrategy();
            T finalEntity = entity;

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            if (strategy == ALWAYS) {
                                redisTemplate.boundValueOps(cacheKey).set(finalEntity, getTimeout(), TimeUnit.DAYS);
                            }
                        }
                    }
            );
        }
        return entity;
    }

    @Override
    @Transactional
    public T updateById(T entity) {
        String cacheKey = this.getCacheKey(entity.getId());
        redisTemplate.delete(cacheKey);

        long currentUser = this.getCurrentUser();
        entity.setUpdateBy(currentUser);
        entity.setUpdateTime(LocalDateTime.now());
        T org = this.mapper.selectById(entity.getId());
        Assert.validateId(org, "实体", entity.getId());

        try {
            this.fieldEditLogService.logUpdation(org, entity);
        } catch (IllegalAccessException var6) {
            throw new CheckedException(var6);
        }

        boolean success = SqlHelper.retBool(this.mapper.updateById(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new CheckedException("数据更新失败！");
        } else {
            entity = this.mapper.selectById(entity.getId());
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            EntityCacheableStrategy strategy = this.getEntityCacheableStrategy();
            T finalEntity = entity;

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            switch (strategy) {
                                case NON_TRANSACTIONAL: {
                                    redisTemplate.delete(cacheKey);
                                    break;
                                }
                                case ALWAYS: {
                                    redisTemplate.boundValueOps(cacheKey).set(finalEntity, getTimeout(), TimeUnit.DAYS);
                                }
                                default:
                            }
                        }
                    }
            );
        }
        return entity;
    }

    @Override
    @Transactional
    public void delete(T entity) {
        String cacheKey = this.getCacheKey(entity.getId());
        redisTemplate.delete(cacheKey);
        super.delete(entity);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            redisTemplate.delete(cacheKey);
                        }
                    }
            );
        }
    }

    /**
     * 获取缓存键值
     */
    protected String getCacheKey(Long id) {
        ParameterizedType p = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type model = p.getActualTypeArguments()[0];
        return model.getTypeName() + ":" + id;
    }

}
