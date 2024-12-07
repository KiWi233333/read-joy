package com.readjoy.readjoyapi.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 资源表
 * @TableName resource
 */
@TableName(value ="resource")
@Data
public class Resource implements Serializable {
    /**
     * 资源ID
     */
    @TableId
    private Integer resourceId;

    /**
     * 资源类型，视频，教学大纲，课件等
     */
    private String type;

    /**
     * 关联的图书ID
     */
    private Integer bookId;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源大小
     */
    private Integer size;

    /**
     * 资源保存地址
     */
    private String url;

    /**
     * 资源提交者
     */
    private String submitter;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 下载数
     */
    private Integer downloadCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}