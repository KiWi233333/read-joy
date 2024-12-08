package com.readjoy.readjoyapi.common.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 管理员修改密码参数
 *
 * @className: AdminUpdatePwdDTO
 * @author: Kiwi23333
 * @date: 2024/12/7 21:28
 */
@Data
public class AdminUpdatePwdDTO {

    @NotBlank(message = "旧密码不能为空")
    @Length(min = 6, max = 20, message = "旧密码长度不正确，请重试！")
    private String oldPassword;
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 20, message = "新密码长度不正确，请重试！")
    private String newPassword;
}
