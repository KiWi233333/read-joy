package com.readjoy.readjoyapi.common.service;


import com.readjoy.readjoyapi.common.dto.user.UserLoginDTO;
import com.readjoy.readjoyapi.common.dto.user.UserRegisterDTO;
import com.readjoy.readjoyapi.common.dto.user.UserUpdateInfoDTO;
import com.readjoy.readjoyapi.common.dto.user.UserUpdatePwdDTO;
import com.readjoy.readjoyapi.common.vo.user.UserInfoVO;
import com.readjoy.readjoyapi.common.vo.user.UserLoginVO;

/**
 * 用户服务层
 *
 * @author 13296
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-12-07 17:55:09
 */
public interface UserService {

    UserLoginVO login(UserLoginDTO loginDTO);

    UserLoginVO register(UserRegisterDTO loginDTO);

    Integer updatePwd(UserUpdatePwdDTO dto);

    UserInfoVO updateUserBaseInfo(UserUpdateInfoDTO dto);

    UserInfoVO getUserInfo();
}
