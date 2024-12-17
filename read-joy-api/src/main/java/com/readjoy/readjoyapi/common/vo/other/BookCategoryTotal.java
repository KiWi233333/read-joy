package com.readjoy.readjoyapi.common.vo.other;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 首页统计数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BookCategoryTotal {

    @Schema(description = "分类id")
    private String categoryId;
    @Schema(description = "分类名称")
    private String categoryName;
    @Schema(description = "发表图书总量")
    private Long bookCount;
}
