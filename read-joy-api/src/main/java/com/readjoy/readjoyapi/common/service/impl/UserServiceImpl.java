package com.readjoy.readjoyapi.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.readjoy.readjoyapi.common.mapper.UserMapper;
import com.readjoy.readjoyapi.common.pojo.User;
import com.readjoy.readjoyapi.common.repository.UserRepository;
import com.readjoy.readjoyapi.common.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author 13296
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-12-07 17:55:09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Resource
    private UserRepository userRepository;

}




