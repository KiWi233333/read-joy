package com.readjoy.readjoyapi.common.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.repository.JoinCrudRepository;
import com.readjoy.readjoyapi.common.dto.admin.AdminUpdatePwdDTO;
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
public class AdminRepository extends JoinCrudRepository<AdminMapper, Admin> {

    public Admin selectByUnamePwd(String username, String password) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, username).eq(Admin::getPassword, password));
    }
    public Admin selectByUsername(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, username));
    }

    public Integer updatePwd(Integer id,AdminUpdatePwdDTO dto) {
        return baseMapper.update(new Admin().setPassword(dto.getNewPassword()), new LambdaQueryWrapper<Admin>()
                .eq(Admin::getId, id)
                .eq(Admin::getPassword, dto.getOldPassword()));
    }
}
