package com.readjoy.readjoyapi.common.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * mybatis plus字段自动填充
 *
 * @className: FillMeteHandler
 * @author: Kiwi23333
 * @description: mybatis plus字段自动填充
 * @date: 2024/12/1 22:41
 */
@Component
public class FillMetaHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入操作时候自动填充
//        Date date = new Date();
//        this.setFieldValByName("updateTime", date, metaObject);
//        this.setFieldValByName("createTime", date, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新操作时自动填充
//        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

}