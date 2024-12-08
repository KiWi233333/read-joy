package com.readjoy.readjoyapi.common.dto.defaults;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;


@Data
@Accessors(chain = true)
public class PageDTO<T> {

    @Schema(description = "页码", example = "1")
    @NotNull(message = "页码不能为空！")
    @Range(min = 1, max = 500, message = "页码范围错误（1-500）！")
    private Integer page = 1;

    @Schema(description = "每页条数", example = "10")
    @Range(min = 1, message = "每页条数范围错误！")
    @NotNull(message = "每页条数不能为空！")
    private Integer size = 15;

    public Page<T> toPage() {
        return new Page<>(page, size);
    }

}
