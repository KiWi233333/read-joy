package com.readjoy.readjoyapi.common.vo.user;

import com.readjoy.readjoyapi.common.pojo.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 登录返回值
 *
 * @className: AdminLoginVO
 * @author: Kiwi23333
 * @date: 2024/12/7 21:28
 */
@Data
@Accessors(chain = true)
public class UserLoginVO {

    @Schema(type = "string", description = "token")
    private String token;


    @Schema(description = "用户信息")
    private UserInfoVO userInfo;

    public static UserLoginVO toVO(User user, String token) {
        return new UserLoginVO().setToken(token).setUserInfo(UserInfoVO.toVO(user));
    }
}
