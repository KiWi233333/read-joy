package com.readjoy.readjoyapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.readjoy.readjoyapi.common.dto.admin.AdminLoginDTO;
import com.readjoy.readjoyapi.common.dto.admin.AdminUpdatePwdDTO;
import com.readjoy.readjoyapi.common.enums.SysUserTypeEnum;
import com.readjoy.readjoyapi.common.pojo.Admin;
import com.readjoy.readjoyapi.common.vo.admin.AdminInfoVO;
import com.readjoy.readjoyapi.repository.AdminRepository;
import com.readjoy.readjoyapi.service.AdminService;
import com.readjoy.readjoyapi.common.utils.*;
import com.readjoy.readjoyapi.common.vo.admin.AdminLoginVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author 13296
 * @description 针对表【admin(管理员表)】的数据库操作Service实现
 * @createDate 2024-12-07 17:55:09
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminRepository adminRepository;

    @Override
    public AdminLoginVO login(AdminLoginDTO loginDTO) {
        // 查询用户
        Admin admin = adminRepository.selectByUnamePwd(loginDTO.getUsername(), loginDTO.getPassword());
        AssertUtil.isNotEmpty(admin, "用户不存在，请稍后再试！");
        // 生成token
        String token = JWTUtil.createToken(new UserTokenUtil()
                .setId(admin.getId())
                .setUserType(SysUserTypeEnum.ADMIN.getType()));
        return new AdminLoginVO()
                .setToken(token)
                .setUsername(admin.getUsername());
    }

    @Override
    public Integer updatePwd(AdminUpdatePwdDTO dto) {
        AssertUtil.isFalse(dto.getOldPassword().equals(dto.getNewPassword()), "新旧密码不能相同！");
        final Integer uid = RequestHolderUtil.get().getId();
        final Admin repositoryOne = adminRepository.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getId, uid));
        AssertUtil.isTrue(repositoryOne.getPassword().equals(dto.getOldPassword()), "旧密码错误，请稍后再试！");
        // 更新密码
        return adminRepository.updatePwd(uid, dto);
    }

    @Override
    public Admin initSuerAdmin(Admin admin) {
        // 判断是否存在
        Admin data = adminRepository.selectByUsername(admin.getUsername());
        if (data != null) {
            return data;
        }
        return adminRepository.save(admin) ? admin : null;
    }

    /**
     * 获取管理员信息
     *
     * @param id 管理员ID
     * @return 管理员信息的视图对象
     */
    @Override
    public AdminInfoVO getAdminInfo(Integer id) {
        Admin admin = adminRepository.getById(id);
        AssertUtil.isNotEmpty(admin, "管理员不存在！");
        return new AdminInfoVO()
                .setId(admin.getId())
                .setUsername(admin.getUsername());
    }
}




