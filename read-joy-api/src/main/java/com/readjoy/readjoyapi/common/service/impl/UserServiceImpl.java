package com.readjoy.readjoyapi.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.readjoy.readjoyapi.common.dto.user.UserLoginDTO;
import com.readjoy.readjoyapi.common.dto.user.UserRegisterDTO;
import com.readjoy.readjoyapi.common.dto.user.UserUpdateInfoDTO;
import com.readjoy.readjoyapi.common.dto.user.UserUpdatePwdDTO;
import com.readjoy.readjoyapi.common.enums.SysUserTypeEnum;
import com.readjoy.readjoyapi.common.mapper.UserMapper;
import com.readjoy.readjoyapi.common.pojo.User;
import com.readjoy.readjoyapi.common.repository.UserRepository;
import com.readjoy.readjoyapi.common.service.UserService;
import com.readjoy.readjoyapi.common.utils.*;
import com.readjoy.readjoyapi.common.vo.user.UserInfoVO;
import com.readjoy.readjoyapi.common.vo.user.UserLoginVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 13296
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-12-07 17:55:09
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Resource
    private LocalFileUtil localFileUtil;
    @Resource
    private UserRepository userRepository;

    @Override
    public UserLoginVO login(UserLoginDTO loginDTO) {
        User user = userRepository.selectByLoginNameAndPwd(loginDTO.getLoginName(), loginDTO.getPassword(), true);
        AssertUtil.isNotEmpty(user, "用户不存在或密码错误！");
        // 生成token
        String token = JWTUtil.createToken(new UserTokenUtil()
                .setUserType(SysUserTypeEnum.CUSTOMER.getType())
                .setId(user.getId()));
        // 返回登录信息
        return UserLoginVO.toVO(user, token);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVO register(UserRegisterDTO loginDTO) {
        // 校验用户名是否存在
        AssertUtil.isFalse(userRepository.existsByLoginName(loginDTO.getLoginName()), "该用户名已被使用，请更换后再试！");
        // 头像
        String imgUrl = null;
        if (loginDTO.getCover() != null) {
            imgUrl = localFileUtil.saveFile(loginDTO.getCover());
        }
        // 生成用户
        final User user = UserRegisterDTO.toUser(loginDTO, imgUrl);
        AssertUtil.isTrue(userRepository.save(user), "用户注册失败，请稍后重试！");
        // 生成token
        String token = JWTUtil.createToken(new UserTokenUtil()
                .setUserType(SysUserTypeEnum.CUSTOMER.getType())
                .setId(user.getId()));
        // 返回登录信息
        return UserLoginVO.toVO(user, token);
    }

    @Override
    public Integer updatePwd(UserUpdatePwdDTO dto) {
        // 校验密码
        AssertUtil.isTrue(!dto.getNewPassword().equals(dto.getOldPassword()), "新旧密码不能相同！");
        // 校验原密码
        User user = userRepository.getById(RequestHolderUtil.get().getId());
        AssertUtil.isNotEmpty(user, "用户不存在！");
        AssertUtil.isTrue(user.getLoginPassword().equals(dto.getOldPassword()), "原密码错误，请重新输入！");
        // 更新密码
        boolean isSuccess = userRepository.updatePwd(RequestHolderUtil.get().getId(), dto.getNewPassword());
        AssertUtil.isTrue(isSuccess, "密码更新失败，请稍后重试！");
        return 1;
    }

    @Override
    public UserInfoVO updateUserBaseInfo(UserUpdateInfoDTO dto) {
        // 查询用户
        User oldUser = userRepository.getById(RequestHolderUtil.get().getId());
        AssertUtil.isNotEmpty(oldUser, "用户不存在！");
        // 校验用户名是否存在
        if (StringUtils.isNotBlank(dto.getLoginName()) && !dto.getLoginName().equals(oldUser.getLoginName())) {
            AssertUtil.isFalse(userRepository.existsByLoginName(dto.getLoginName()), "该用户名已被使用，请更换后再试！");
        }
        // 头像
        String imgUrl = null;
        if (dto.getCover() != null) {
            imgUrl = localFileUtil.saveFile(dto.getCover());
            // 删除旧头像
            if (StringUtils.isNotBlank(oldUser.getImgUrl())) {
                if (localFileUtil.deleteFile(oldUser.getImgUrl())) {
                    log.info("删除旧头像成功, url={}", oldUser.getImgUrl());
                } else {
                    log.error("删除旧头像失败,uid={}, url={}", oldUser.getId(), oldUser.getImgUrl());
                }
            }
        }
        // 更新用户信息
        User newUser = UserUpdateInfoDTO.toUser(dto, imgUrl);
        newUser.setId(oldUser.getId());
        boolean isSuccess = userRepository.updateById(newUser);
        AssertUtil.isTrue(isSuccess, "用户信息更新失败，请稍后重试！");
        // 返回用户信息
        return UserInfoVO.toVO(newUser);
    }

    @Override
    public UserInfoVO getUserInfo() {
        User user = userRepository.getById(RequestHolderUtil.get().getId());
        AssertUtil.isNotEmpty(user, "用户不存在！");
        // 返回用户信息
        return UserInfoVO.toVO(user);
    }
}




