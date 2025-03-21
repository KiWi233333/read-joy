package com.readjoy.readjoyapi.common.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "用户实体类")
public class User implements Serializable {
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
     * 登录密码
     */
    @Schema(description = "用户登录密码", example = "password123")
    private String loginPassword;

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

    @Schema(description = "创建时间", example = "2023-01-01T12:00:00Z")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 用户类型
     */
    @Schema(description = "用户类型，1：普通用户，2：管理员", example = "1")
    private Integer userType;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}