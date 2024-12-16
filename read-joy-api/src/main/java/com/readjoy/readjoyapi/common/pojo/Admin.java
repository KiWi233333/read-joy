package com.readjoy.readjoyapi.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 管理员表
 * @TableName admin
 */
@TableName(value ="admin")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 管理员用户名
     */
    private String username;

    /**
     * 管理员密码
     */
    private String password;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}