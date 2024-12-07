package com.readjoy.readjoyapi.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String loginPassword;

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 用户头像URL
     */
    private String imgUrl;

    /**
     * 是否审核通过
     */
    private Integer isChecked;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户类型
     */
    private Integer userType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}