package com.readjoy.readjoyapi.common.dto.user;

import com.readjoy.readjoyapi.common.annotation.Password;
import com.readjoy.readjoyapi.common.annotation.Phone;
import com.readjoy.readjoyapi.common.enums.BoolEnum;
import com.readjoy.readjoyapi.common.enums.CustomerUserTypeEnum;
import com.readjoy.readjoyapi.common.pojo.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户注册参数
 *
 * @author: Kiwi23333
 * @date: 2024/12/7 21:28
 */
@Data
public class UserRegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 6, max = 20, message = "用户名长度不正确！")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "用户名只能包含字母和数字！")
    private String loginName;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度不正确，请重试！")
    @Password(message = "密码格式不正确！")
    private String loginPassword;

    @Schema(description = "头像URL")
    private MultipartFile cover = null;

    @Schema(description = "真实姓名")
    private String trueName;

    @Schema(description = "电话号码")
    @Phone(message = "手机号码格式不正确！")
    private String telephone;

    public static User toUser(UserRegisterDTO userRegisterDTO, String imgUrl) {
        return new User()
                .setLoginName(userRegisterDTO.getLoginName())
                .setLoginPassword(userRegisterDTO.getLoginPassword())
                .setTrueName(userRegisterDTO.getTrueName())
                .setTelephone(userRegisterDTO.getTelephone())
                // 单独处理
                .setImgUrl(imgUrl)
                .setIsChecked(BoolEnum.TRUE.getValue())
                .setUserType(CustomerUserTypeEnum.NORMAL.getType());
    }

}
