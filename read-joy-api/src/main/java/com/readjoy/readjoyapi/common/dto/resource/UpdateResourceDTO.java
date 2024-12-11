package com.readjoy.readjoyapi.common.dto.resource;

import com.baomidou.mybatisplus.annotation.TableField;
import com.readjoy.readjoyapi.common.enums.ResourceTypeEnum;
import com.readjoy.readjoyapi.common.pojo.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


@Data
@Accessors(chain = true)
public class UpdateResourceDTO {

    /**
     * @see ResourceTypeEnum
     */
    @Schema(description = "资源类型", example = "视频")
    @Range(min = 1, max = 7, message = "资源类型错误！")
    private Integer type;

    /**
     * 关联的图书ID
     */
    @Schema(description = "关联的图书ID", example = "1001")
    @Range(min = 1, message = "关联的图书ID必须大于0！")
    private Integer bookId;

    /**
     * 资源标题
     */
    @Schema(description = "资源标题", example = "Java 编程教程")
    @Length(min = 1, max = 100, message = "资源标题长度必须在1到100之间！")
    private String title;

    @Schema(description = "资源文件")
    @NotNull(message = "资源文件不能为空！")
    private MultipartFile resourceFile = null;


    /**
     * 是否删除
     */
    @Schema(description = "是否删除 0 - 未删除，1 - 已删除", example = "0")
    @Range(min = 0, max = 1, message = "是否删除只能为0或1！")
    private Integer isDeleted = 0;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static Resource toResource(UpdateResourceDTO dto, String fileUrl, Long size, String submitter) {
        return new Resource()
                .setType(Objects.requireNonNull(ResourceTypeEnum.fromCode(dto.getType())).getValue())
                .setBookId(dto.getBookId())
                .setTitle(dto.getTitle())
                .setSize(size)
                .setUrl(fileUrl)
                .setSubmitter(submitter)
                .setIsDeleted(dto.getIsDeleted());
    }
}
