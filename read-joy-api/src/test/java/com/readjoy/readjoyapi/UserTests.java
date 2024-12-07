package com.readjoy.readjoyapi;

import com.readjoy.readjoyapi.common.pojo.User;
import com.readjoy.readjoyapi.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class UserTests {

    @Autowired
    private UserService userService;

    @Test
    void testAdmin() {
        User user = userService.getUserByUsername("1");
        log.info(String.valueOf(user));
    }

}
