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
public class AdminInfoVO {

    @Schema(type = "integer", description = "管理员ID")
    private Integer id;

    @Schema(type = "string", description = "管理员")
    private String username;

}
