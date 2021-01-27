package com.huali.utils.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.huali.utils.base.service.FieldEditLogService;
import com.huali.utils.exception.Assert;
import com.huali.utils.exception.CheckedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author myUser
 * @date 2021-01-23 16:18
 **/
@Slf4j
public abstract class BaseService<T extends BaseModel, M extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>> {

    @Autowired
    protected M mapper;

    @Autowired
    private FieldEditLogService fieldEditLogService;

    public static final String DEFAULT_ORDER_COLUMN = "id_";

    /**
     * 通过SysLogInterceptor日志拦截器来设置
     */
    public static final ThreadLocal<Long> CURRENT_USER = new ThreadLocal<>();

    protected long getCurrentUser() {
        Long currentUser = CURRENT_USER.get();
        return currentUser != null ? currentUser : 0L;
    }

    @Transactional
    public T insert(T entity) {
        long currentUser = this.getCurrentUser();

        LocalDateTime now = LocalDateTime.now();
        entity.setId(null);
        entity.setCreateBy(currentUser);
        entity.setCreateTime(now);
        entity.setUpdateBy(currentUser);
        entity.setUpdateTime(now);

        boolean success = SqlHelper.retBool(mapper.insert(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new CheckedException("数据插入失败！");
        }

        return this.getById(entity.getId());
    }

    @Transactional
    public T updateById(T entity) {
        long currentUser = this.getCurrentUser();
        entity.setUpdateBy(currentUser);
        entity.setUpdateTime(LocalDateTime.now());

        T org = this.getById(entity.getId());
        Assert.validateId(org, "实体", entity.getId());

        try {
            fieldEditLogService.logUpdation(org, entity);
        } catch (IllegalAccessException e) {
            throw new CheckedException(e);
        }

        boolean success = SqlHelper.retBool(mapper.updateById(entity));
        if (!success) {
            log.error("实体信息：" + entity.toString());
            throw new CheckedException("数据更新失败！");
        }

        return this.getById(entity.getId());
    }

    @Transactional
    public T saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            return this.insert(entity);
        } else {
            return this.updateById(entity);
        }
    }

    @Transactional
    public void delete(Long id) {
        T entity = this.getById(id);
        if (entity == null) {
            throw new CheckedException("无效的ID：" + id);
        }
        this.delete(entity);
    }

    @Transactional
    public void delete(T entity) {
        if (entity.getId() == null) {
            throw new CheckedException("无法删除ID为空的实体");
        }

        long currentUser = this.getCurrentUser();
        entity.setUpdateBy(currentUser);
        entity.setUpdateTime(LocalDateTime.now());
        this.updateById(entity);

        mapper.deleteById(entity.getId());
    }

    @Transactional
    public void delete(Wrapper<T> wrapper) {
        List<T> entitys = mapper.selectList(wrapper);
        for (T entity : entitys) {
            this.delete(entity.getId());
        }
    }

    public T getById(Long id) {
        return mapper.selectById(id);
    }

    public T unique(Wrapper<T> queryWrapper) {
        return this.unique(queryWrapper, true);
    }

    public T unique(Wrapper<T> queryWrapper, boolean throwEx) {
        if (throwEx) {
            return mapper.selectOne(queryWrapper);
        }

        List<T> list = mapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            int size = list.size();
            if (size > 1) {
                log.warn(String.format("Warn: execute Method There are  %s results.", size));
            }
            return list.get(0);
        } else {
            return null;
        }
    }

    public Page<T> query(Page<T> page, Wrapper<T> queryWrapper) {
        if (page.getOrders().size() == 0) {
            page.addOrder(new OrderItem(DEFAULT_ORDER_COLUMN, false));
        }
        return mapper.selectPage(page, queryWrapper);
    }

    public Page<Map<String, Object>> queryMaps(Page<Map<String, Object>> page, Wrapper<T> queryWrapper) {
        if (page.getOrders().size() == 0) {
            page.addOrder(new OrderItem(DEFAULT_ORDER_COLUMN, false));
        }
        return mapper.selectMapsPage(page, queryWrapper);
    }

    public List<T> list() {
        return this.list(Wrappers.emptyWrapper());
    }

    public List<T> list(Wrapper<T> queryWrapper) {
        return mapper.selectList(queryWrapper);
    }

    public List<T> listByMap(Map<String, Object> columnMap) {
        return mapper.selectByMap(columnMap);
    }

    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return mapper.selectMaps(queryWrapper);
    }

    public int count(Wrapper<T> queryWrapper) {
        return SqlHelper.retCount(mapper.selectCount(queryWrapper));
    }

    public boolean exists(Wrapper<T> queryWrapper) {
        return this.count(queryWrapper) > 0;
    }

    @SuppressWarnings("unchecked")
    public <N> List<N> listObjects(Wrapper<T> queryWrapper) {
        List<Object> objects = mapper.selectObjs(queryWrapper);
        return (List<N>) objects;
    }
}
