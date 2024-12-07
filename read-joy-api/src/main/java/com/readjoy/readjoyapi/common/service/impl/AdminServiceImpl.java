package com.readjoy.readjoyapi.common.service.impl;

import com.readjoy.readjoyapi.common.dto.admin.AdminLoginDTO;
import com.readjoy.readjoyapi.common.dto.admin.AdminUpdatePwdDTO;
import com.readjoy.readjoyapi.common.pojo.Admin;
import com.readjoy.readjoyapi.common.repository.AdminRepository;
import com.readjoy.readjoyapi.common.service.AdminService;
import com.readjoy.readjoyapi.common.utils.AssertUtil;
import com.readjoy.readjoyapi.common.utils.PwdUtil;
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

        Admin admin = adminRepository.selectByUsername(loginDTO.getUsername());
        AssertUtil.isNotEmpty(admin, "用户不存在，请稍后再试！");

        final boolean checkPwd = PwdUtil.check(loginDTO.getPassword(), admin.getPassword());
        AssertUtil.isTrue(checkPwd, "密码错误，请稍后再试！");
        // 生成token
        return new AdminLoginVO().setToken(admin.getPassword());
    }

    @Override
    public Integer updatePwd(AdminUpdatePwdDTO dto) {
        return null;
    }
}




