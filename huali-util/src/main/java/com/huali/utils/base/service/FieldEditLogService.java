package com.huali.utils.base.service;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huali.utils.base.BaseModel;
import com.huali.utils.base.mapper.FieldEditLogMapper;
import com.huali.utils.base.model.FieldEditLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字段编辑日志表 服务实现类
 * </p>
 *
 * @author lvws
 * @since 2019-04-10
 */
@Service
public class FieldEditLogService {

    private static final String ID = "id";
    private static final String CREATE_BY = "createBy";
    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_BY = "updateBy";
    private static final String UPDATE_TIME = "updateTime";
    private static final String SERIAL_VERSION_UID = "serialVersionUID";

    private static final List<String> FILTER_FIELDS = new ArrayList<String>() {{
        add(ID);
        add(CREATE_BY);
        add(CREATE_TIME);
        add(UPDATE_BY);
        add(UPDATE_TIME);
        add(SERIAL_VERSION_UID);
    }};

    @Autowired
    private FieldEditLogMapper fieldEditLogMapper;
    @Value("${mybatis-plus.global-config.db-config.update-strategy}")
    private FieldStrategy fieldStrategy;

    @Transactional
    public <T extends BaseModel> void logUpdation(T org, T update) throws IllegalAccessException {
        Long recordId = update.getId();
        Long lastUpdateBy = org.getUpdateBy();
        LocalDateTime lastUpdateTime = org.getUpdateTime();
        Long currentUpdateBy = update.getUpdateBy();
        LocalDateTime currentUpdateTime = update.getUpdateTime();

        Field[] fields = update.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (FILTER_FIELDS.contains(field.getName())) {
                continue;
            }

            field.setAccessible(true);
            Object newValue = field.get(update);

            TableField annotation = field.getAnnotation(TableField.class);
            FieldStrategy strategy;
            if (annotation != null) {
                //过滤临时字段
                if (!annotation.exist()) {
                    continue;
                }
                strategy = annotation.updateStrategy();
            } else {
                strategy = fieldStrategy;
            }

            if (strategy == FieldStrategy.DEFAULT) {
                strategy = fieldStrategy;
            }

            if (strategy == FieldStrategy.NOT_EMPTY) {
                //如果更新策略是非空更新
                if (newValue instanceof String && StringUtils.isBlank((String) newValue)) {
                    //如果是空字符串，则不会更新
                    continue;
                } else if (newValue == null) {
                    //如果是nul，也不会更新
                    continue;
                }
            } else if (strategy == FieldStrategy.NOT_NULL && newValue == null) {
                //如果更新策略是非null更新，新值是null，则不会更新
                continue;
            }

            Object oldValue = field.get(org);
            if (newValue == null && oldValue == null) {
                //新旧值都为null，值不变
                continue;
            } else if (newValue != null && newValue.equals(oldValue)) {
                //新旧值都不为null，新值等于旧值，值不变
                continue;
            }

            FieldEditLog fieldEditLog = new FieldEditLog();
            fieldEditLog.setClassName(update.getClass().getName());
            fieldEditLog.setFieldName(field.getName());
            fieldEditLog.setRecordId(recordId);
            fieldEditLog.setOldValue(String.valueOf(oldValue));
            fieldEditLog.setNewValue(String.valueOf(newValue));
            fieldEditLog.setCreateBy(lastUpdateBy);
            fieldEditLog.setCreateTime(lastUpdateTime);
            fieldEditLog.setUpdateBy(currentUpdateBy);
            fieldEditLog.setUpdateTime(currentUpdateTime);
            fieldEditLogMapper.insert(fieldEditLog);
        }
    }

    public IPage<FieldEditLog> page(IPage<FieldEditLog> page, Wrapper<FieldEditLog> wrapper) {
        return fieldEditLogMapper.selectPage(page, wrapper);
    }
}
