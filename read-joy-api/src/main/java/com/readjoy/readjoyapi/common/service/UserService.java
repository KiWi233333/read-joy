package com.readjoy.readjoyapi.common.service;

import com.readjoy.readjoyapi.common.pojo.User;

/**
 * 用户服务层
 *
 * @author 13296
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-12-07 17:55:09
 */
public interface UserService {
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    User getUserByUsername(String username);
}
