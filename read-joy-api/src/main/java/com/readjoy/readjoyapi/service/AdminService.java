package com.readjoy.readjoyapi.service;

import com.readjoy.readjoyapi.common.dto.admin.AdminDataDTO;
import com.readjoy.readjoyapi.common.dto.admin.AdminLoginDTO;
import com.readjoy.readjoyapi.common.dto.admin.AdminUpdatePwdDTO;
import com.readjoy.readjoyapi.common.pojo.Admin;
import com.readjoy.readjoyapi.common.vo.admin.AdminInfoVO;
import com.readjoy.readjoyapi.common.vo.admin.AdminLoginVO;
import com.readjoy.readjoyapi.common.vo.other.AdminHomeStatisticsVO;

/**
 * 管理员服务层
 *
 * @author 13296
 * @description 针对表【admin(管理员表)】的数据库操作Service
 * @createDate 2024-12-07 17:55:09
 */
public interface AdminService {
    /**
     * 根据管理员登录信息进行登录
     *
     * @param loginDTO 管理员登录信息的数据传输对象
     * @return 管理员登录后的视图对象
     */
    AdminLoginVO login(AdminLoginDTO loginDTO);

    /**
     * 更新管理员密码
     *
     * @param dto 包含旧密码和新密码的数据传输对象
     * @return 更新操作影响的记录数
     */
    Integer updatePwd(AdminUpdatePwdDTO dto);

    /**
     * 初始化超级管理员
     *
     * @param admin 超级管理员的信息
     * @return 初始化后的超级管理员对象
     */
    Admin initSuerAdmin(Admin admin);

    /**
     * 获取管理员信息
     *
     * @param id 管理员ID
     * @return 管理员信息的视图对象
     */
    AdminInfoVO getAdminInfo(Integer id);

    /**
     * 获取管理员首页统计信息
     * @param adminDataDTO 参数
     * @return 管理员首页统计信息的视图对象
     */
    AdminHomeStatisticsVO getHomeStatistics(AdminDataDTO adminDataDTO);
}
