package com.readjoy.readjoyapi.common.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.repository.JoinCrudRepository;
import com.readjoy.readjoyapi.common.mapper.UserMapper;
import com.readjoy.readjoyapi.common.pojo.User;
import org.springframework.stereotype.Component;

/**
 * @className: UserRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class UserRepository extends JoinCrudRepository<UserMapper, User> {

    public User selectOntByLoginName(String loginName) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getLoginName, loginName));
    }
}
