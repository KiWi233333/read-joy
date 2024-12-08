package com.readjoy.readjoyapi.common.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 管理员登录返回值
 *
 * @className: AdminLoginVO
 * @author: Kiwi23333
 * @date: 2024/12/7 21:28
 */
@Data
@Accessors(chain = true)
public class AdminLoginVO {

    @Schema(type = "string", description = "token")
    private String token;

    @Schema(type = "string", description = "用户名")
    private String username;


}
