package com.readjoy.readjoyapi.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.user.*;
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

    IPage<UserInfoVO> getPageByDTO(SelectUserPageDTO dto);

    Integer batchDeleteUser(Integer[] ids);

    /**
     * 更新用户状态
     * @param id 用户id
     * @param status 用户状态
     * @return 更新结果
     */
    Integer updateUserStatus(Integer id, Integer status);
}
