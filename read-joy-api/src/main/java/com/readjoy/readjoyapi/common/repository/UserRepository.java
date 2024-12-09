package com.readjoy.readjoyapi.common.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.repository.JoinCrudRepository;
import com.readjoy.readjoyapi.common.dto.user.SelectUserPageDTO;
import com.readjoy.readjoyapi.common.mapper.UserMapper;
import com.readjoy.readjoyapi.common.pojo.Category;
import com.readjoy.readjoyapi.common.pojo.User;
import com.readjoy.readjoyapi.common.vo.user.UserInfoVO;
import org.springframework.stereotype.Component;

/**
 * @className: UserRepository
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 18:22
 */
@Component
public class UserRepository extends JoinCrudRepository<UserMapper, User> {

    public User selectByLoginNameAndPwd(String loginName, String password, Boolean isChecked) {
        final LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<User>()
                .select(User::getId,
                        User::getLoginName,
                        User::getTelephone,
                        User::getImgUrl,
                        User::getIsChecked,
                        User::getTrueName,
                        User::getUserType,
                        User::getCreateTime
                )
                .eq(User::getLoginName, loginName)
                .eq(User::getLoginPassword, password);
        if (isChecked!= null) {
            qw.eq(User::getIsChecked, isChecked);
        }
        return this.getOne(qw);
    }

    public boolean updatePwd(Integer id, String newPassword) {
        return this.update(
                new User().setLoginPassword(newPassword),
                new LambdaQueryWrapper<User>()
                        .eq(User::getId, id));
    }

    /**
     * 判断是否存在该用户名
     *
     * @param loginName 用户名
     * @return true：存在，false：不存在
     */
    public boolean existsByLoginName(String loginName) {
        return this.exists(new LambdaQueryWrapper<User>()
                .eq(User::getLoginName, loginName));
    }

    public IPage<UserInfoVO> selectPageByDTO(SelectUserPageDTO dto) {
        final LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<User>()
                .select(
                        User::getId,
                        User::getLoginName,
                        User::getTelephone,
                        User::getImgUrl,
                        User::getIsChecked,
                        User::getTrueName,
                        User::getUserType,
                        User::getCreateTime
                )
                .and(StringUtils.isNotBlank(dto.getKeyword()), q -> q // 关键字
                        .or().like(User::getLoginName, dto.getKeyword())
                        .or().like(User::getTelephone, dto.getKeyword())
                        .or().like(User::getTrueName, dto.getKeyword()))
                .eq(dto.getIsChecked() != null, User::getIsChecked, dto.getIsChecked());
        qw.orderBy(dto.getCreateOrder() != null, dto.checkOrderAsc(), User::getCreateTime);
        final Page<User> userPage = new Page<>(dto.getPage(), dto.getSize());
        final IPage<User> page = this.page(userPage, qw);
        return page.convert(UserInfoVO::toVO);
    }

    public boolean updateStatus(Integer id, Integer status) {
        return this.update(
                new User().setIsChecked(status),
                new LambdaQueryWrapper<User>()
                        .eq(User::getId, id));
    }
}
