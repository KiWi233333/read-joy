package com.readjoy.readjoyapi.common.service;

import com.readjoy.readjoyapi.common.dto.admin.AdminLoginDTO;
import com.readjoy.readjoyapi.common.dto.admin.AdminUpdatePwdDTO;
import com.readjoy.readjoyapi.common.vo.admin.AdminLoginVO;

/**
 * 管理员服务层
 *
 * @author 13296
 * @description 针对表【admin(管理员表)】的数据库操作Service
 * @createDate 2024-12-07 17:55:09
 */
public interface AdminService {

    AdminLoginVO login(AdminLoginDTO loginDTO);

    Integer updatePwd(AdminUpdatePwdDTO dto);
}
