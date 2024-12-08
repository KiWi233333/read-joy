package com.readjoy.readjoyapi.common.config.auth;


import com.readjoy.readjoyapi.common.pojo.Admin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@PropertySource(value = "classpath:application.properties")
public class SuperAdminConfig {

    @Value("${surper.admin.username}")
    private String username;
    @Value("${surper.admin.password}")
    private String password;

    public Admin getAdmin() {
        return new Admin().setUsername(username).setPassword(password);
    }
}
