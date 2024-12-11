package com.readjoy.readjoyapi.common.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 资源表
 * @TableName resource
 */
@TableName(value ="resource")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "资源实体类", name = "Resource")
public class Resource implements Serializable {

    /**
     * 资源ID
     */
    @TableId
    @Schema(description = "资源ID", example = "1")
    private Integer resourceId;

    /**
     * 资源类型，视频，教学大纲，课件等
     */
    @Schema(description = "资源类型", example = "视频")
    private String type;

    /**
     * 关联的图书ID
     */
    @Schema(description = "关联的图书ID", example = "1001")
    private Integer bookId;

    /**
     * 资源标题
     */
    @Schema(description = "资源标题", example = "Java 编程教程")
    private String title;

    /**
     * 资源大小
     */
    @Schema(description = "资源大小 (单位: 字节)", example = "204800")
    private Long size;

    /**
     * 资源保存地址
     */
    @Schema(description = "资源保存地址")
    private String url;

    /**
     * 资源提交者
     */
    @Schema(description = "资源提交者", example = "张三")
    private String submitter;

    /**
     * 是否删除
     */
    @Schema(description = "是否删除", example = "0")
    private Integer isDeleted;

    /**
     * 下载数
     */
    @Schema(description = "下载数", example = "150")
    private Integer downloadCount;

    /**
     * 点赞数
     */
    @Schema(description = "点赞数", example = "25")
    private Integer likeCount;

    @Schema(description = "创建时间", example = "2023-01-01T12:00:00Z")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
