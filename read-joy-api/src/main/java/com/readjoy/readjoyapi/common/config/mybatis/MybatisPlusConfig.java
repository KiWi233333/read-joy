package com.readjoy.readjoyapi.common.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置类
 *
 * @className: MybatisPlusConfig
 * @author: Kiwi23333
 * @description: MybatisPlus配置类
 * @date: 2024/12/1 22:41
 */
@Configuration
@MapperScan("com.readjoy.readjoyapi.common.mapper")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();// 分页查询的拦截器
        pageInterceptor.setOptimizeJoin(true);// SQL 查询优化
        pageInterceptor.setDbType(DbType.MYSQL);// 数据库类型
        pageInterceptor.setOverflow(false);// 开启溢出总页数的支持(溢出依然显示第一页)
        pageInterceptor.setMaxLimit(500L);// 单次分页最大数量
        interceptor.addInnerInterceptor(pageInterceptor);// 添加
        OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();//实现乐观锁的拦截器
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);// 添加
        return interceptor;
    }

}
