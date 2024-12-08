package com.readjoy.readjoyapi.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * UserTokenDTO token包含内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserTokenUtil {
    public static final String HEADER_NAME = "Authorization";

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户类型 0 用户 1 管理员
     *
     * @see UserType
     */
    private Integer userType;

    public boolean checkAdmin() {
        return UserType.ADMIN.getType() == userType;
    }

    public boolean checkCustomer() {
        return UserType.CUSTOMER.getType() == userType;
    }
}
