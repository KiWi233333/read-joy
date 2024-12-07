package com.readjoy.readjoyapi.common.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.readjoy.readjoyapi.common.mapper.AdminMapper;
import com.readjoy.readjoyapi.common.pojo.Admin;
import org.springframework.stereotype.Component;

/**
 * @className: AdminDAO
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class AdminRepository extends CrudRepository<AdminMapper, Admin> {

    public Admin selectByUsername(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, username));
    }
}
