package com.readjoy.readjoyapi.common.vo.resource;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 资源VO对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResourceVO implements Serializable {
    @Schema(description = "资源ID")
    private Integer resourceId;

    @Schema(description = "资源类型：application/pdf, image/jpeg, video/mp4, audio/mp3, 等")
    private String type;

    @Schema(description = "关联的图书ID")
    private Integer bookId;

    @Schema(description = "关联的图书标题")
    private String bookTitle;

    @Schema(description = "资源标题")
    private String title;

    @Schema(description = "资源大小（单位：字节）")
    private Integer size;

    @Schema(description = "资源保存地址")
    private String url;

    @Schema(description = "资源提交者")
    private String submitter;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "是否删除（0 - 否，1 - 是）")
    private Integer isDeleted;

    @Schema(description = "下载数")
    private Integer downloadCount;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}