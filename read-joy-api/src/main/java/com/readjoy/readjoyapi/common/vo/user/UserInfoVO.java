package com.readjoy.readjoyapi.common.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.readjoy.readjoyapi.common.pojo.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户信息 VO
 */
@Data
@Accessors(chain = true)
public class UserInfoVO {

    /**
     * 用户ID
     */
    @Schema(description = "用户唯一标识符，自动生成", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 登录名
     */
    @Schema(description = "用户登录名", example = "user123")
    private String loginName;

    /**
     * 真实姓名
     */
    @Schema(description = "用户的真实姓名", example = "张三")
    private String trueName;

    /**
     * 电话号码
     */
    @Schema(description = "用户电话号码", example = "13800138000")
    private String telephone;

    /**
     * 用户头像URL
     */
    @Schema(description = "用户头像的URL地址", example = "http://example.com/image.jpg")
    private String imgUrl;

    /**
     * 是否审核通过
     */
    @Schema(description = "用户是否审核通过，1：通过，0：未通过", example = "1")
    private Integer isChecked;

    /**
     * 创建时间
     */
    @Schema(description = "用户创建时间", example = "2023-01-01T12:00:00Z")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 用户类型
     */
    @Schema(description = "用户类型，1：普通用户", example = "1")
    private Integer userType;

    public static UserInfoVO toVO(User user) {
        return new UserInfoVO()
                .setId(user.getId())
                .setLoginName(user.getLoginName())
                .setTrueName(user.getTrueName())
                .setTelephone(user.getTelephone())
                .setImgUrl(user.getImgUrl())
                .setIsChecked(user.getIsChecked())
                .setCreateTime(user.getCreateTime())
                .setUserType(user.getUserType());
    }
}
