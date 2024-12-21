package com.readjoy.readjoyapi.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.readjoy.readjoyapi.common.dto.user.*;
import com.readjoy.readjoyapi.common.enums.ResultStatus;
import com.readjoy.readjoyapi.common.enums.SysUserTypeEnum;
import com.readjoy.readjoyapi.common.mapper.UserMapper;
import com.readjoy.readjoyapi.common.pojo.User;
import com.readjoy.readjoyapi.repository.UserRepository;
import com.readjoy.readjoyapi.service.UserService;
import com.readjoy.readjoyapi.common.utils.*;
import com.readjoy.readjoyapi.common.vo.user.UserInfoVO;
import com.readjoy.readjoyapi.common.vo.user.UserLoginVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

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
        User user = userRepository.selectByLoginNameAndPwd(loginDTO.getLoginName(), loginDTO.getPassword(), null);
        AssertUtil.isNotEmpty(user, "用户不存在或密码错误！");
        // 校验用户状态
        AssertUtil.isTrue(user.getIsChecked() == 1, ResultStatus.STATUS_OFF_ERR, "账号被封禁，详情联系客服！");
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
        // 判断是否为空对象
        AssertUtil.isFalse(dto.checkEmpty(), "修改信息不能为空！");
        // 查询用户
        final Integer uid = RequestHolderUtil.get().getId();
        User oldUser = userRepository.getById(uid);
        AssertUtil.isNotEmpty(oldUser, "用户不存在！");
        // 校验用户名是否存在
        if (StringUtils.isNotBlank(dto.getLoginName()) && !dto.getLoginName().equals(oldUser.getLoginName())) {
            AssertUtil.isFalse(userRepository.existsByLoginName(dto.getLoginName()), "该用户名已被使用，请更换后再试！");
        }
        // 头像
        String imgUrl = null;
        if (dto.getCover() != null) {
            imgUrl = localFileUtil.saveFile(dto.getCover(), String.valueOf(uid));
        }
        // 更新用户信息
        User newUser = UserUpdateInfoDTO.toUser(dto, imgUrl, true);
        newUser.setId(oldUser.getId());
        boolean isSuccess = userRepository.updateById(newUser);
        if (!isSuccess && imgUrl != null) {
            localFileUtil.deleteFile(imgUrl);
        } else if (StringUtils.isNotBlank(imgUrl) && StringUtils.isNotBlank(oldUser.getImgUrl())) { // 更新头像
            localFileUtil.deleteFile(oldUser.getImgUrl());
        }
        AssertUtil.isTrue(isSuccess, "用户信息更新失败，请稍后重试！");
        // 返回用户信息
        return this.getUserInfo();
    }

    @Override
    public UserInfoVO getUserInfo() {
        User user = userRepository.getById(RequestHolderUtil.get().getId());
        AssertUtil.isNotEmpty(user, "用户不存在！");
        // 返回用户信息
        return UserInfoVO.toVO(user);
    }


    @Override
    public IPage<UserInfoVO> getPageByDTO(SelectUserPageDTO dto) {
        return userRepository.selectPageByDTO(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteUser(Integer[] ids) {
        long count = userRepository.getBaseMapper().deleteByIds(Arrays.asList(ids));
        AssertUtil.isTrue(count == ids.length, "部分删除用户失败，请稍后重试！");
        return ids.length;
    }

    /**
     * 更新用户状态
     *
     * @param id     用户id
     * @param status 用户状态
     * @return 更新结果
     */
    @Override
    public Integer updateUserStatus(Integer id, Integer status) {
        AssertUtil.isNotEmpty(id, "用户id不能为空！");
        AssertUtil.isNotEmpty(status, "用户状态不能为空！");
        // status: 0-禁用，1-启用
        AssertUtil.isTrue(status == 0 || status == 1, "用户状态不正确！");
        AssertUtil.isTrue(userRepository.updateStatus(id, status), "用户状态更新失败，请稍后重试！");
        return 1;
    }
}




