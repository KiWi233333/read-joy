package com.readjoy.readjoyapi;

import com.readjoy.readjoyapi.common.config.auth.SuperAdminConfig;
import com.readjoy.readjoyapi.common.pojo.Admin;
import com.readjoy.readjoyapi.common.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
@Slf4j
public class ReadJoyApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(ReadJoyApiApplication.class, args);
    }

    @Bean
    @Profile({"dev", "test"}) // 只在开发和测试环境中创建这个bean
    public ApplicationRunner runner(AdminService adminService, SuperAdminConfig superAdminConfig) {
        return args -> {
            final Admin admin = adminService.initSuerAdmin(superAdminConfig.getAdmin());
            log.info("超级管理员 -> 账号: {} 密码：{}", admin.getUsername(), "123456");
        };
    }
}
