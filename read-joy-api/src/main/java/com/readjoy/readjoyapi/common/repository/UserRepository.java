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

    public User selectByLoginNameAndPwd(String loginName, String password, Boolean isChecked) {
        return this.getOne(new LambdaQueryWrapper<User>()
                .select(User::getId,
                        User::getLoginName,
                        User::getTelephone,
                        User::getImgUrl,
                        User::getIsChecked,
                        User::getTrueName,
                        User::getUserType,
                        User::getCreateTime
                )
                .eq(isChecked != null, User::getIsChecked, isChecked)
                .eq(User::getLoginName, loginName)
                .eq(User::getLoginPassword, password));
    }

    public boolean updatePwd(Integer id, String newPassword) {
        return this.update(
                new User().setLoginPassword(newPassword),
                new LambdaQueryWrapper<User>()
                        .eq(User::getId, id));
    }

    /**
     * 判断是否存在该用户名
     *
     * @param loginName 用户名
     * @return true：存在，false：不存在
     */
    public boolean existsByLoginName(String loginName) {
        return this.exists(new LambdaQueryWrapper<User>()
                .eq(User::getLoginName, loginName));
    }
}
