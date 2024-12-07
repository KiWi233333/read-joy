package com.readjoy.readjoyapi.common.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 管理员登录参数
 *
 * @className: AdminLoginDTO
 * @author: Kiwi23333
 * @date: 2024/12/7 21:28
 */
@Data
public class AdminLoginDTO {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 4, max = 20, message = "用户名长度不正确！")
    private String username;


    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 50, message = "密码长度不正确，请重试！")
    private String password;
}
