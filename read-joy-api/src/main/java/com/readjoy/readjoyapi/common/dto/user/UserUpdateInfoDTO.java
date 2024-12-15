package com.readjoy.readjoyapi.common.dto.user;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.readjoy.readjoyapi.common.annotation.Phone;
import com.readjoy.readjoyapi.common.pojo.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * 修改用户基本参数
 *
 * @author: Kiwi23333
 * @date: 2024/12/7 21:28
 */
@Data
@Schema(description = "修改用户基本参数")
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateInfoDTO {

    @Length(min = 6, max = 20, message = "用户名长度不正确！")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "用户名只能包含字母和数字！")
    private String loginName;

    @Schema(description = "头像URL")
    private MultipartFile cover = null;

    @Schema(description = "真实姓名")
    private String trueName;

    @Schema(description = "电话号码")
    @Phone(message = "手机号码格式不正确！")
    private String telephone;

    public static User toUser(UserUpdateInfoDTO dto, String imgUrl, boolean isFiltered) {
        // 过滤敏感词
        String trueName = dto.getTrueName();
        if (isFiltered && StringUtils.isNotBlank(trueName)) {
            trueName = SensitiveWordHelper.replace(dto.getTrueName());
        }
        return new User()
                .setLoginName(dto.getLoginName())
                .setTrueName(trueName)
                .setTelephone(dto.getTelephone())
                .setImgUrl(StringUtils.isBlank(imgUrl) ? null : imgUrl)
                ;
    }

}
