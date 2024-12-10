package com.readjoy.readjoyapi.common.vo.category;

import com.readjoy.readjoyapi.common.pojo.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * CategoryVO class.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryVO {

    @Schema(description = "分类ID", example = "1")
    private Integer categoryId;

    @Schema(description = "书名", example = "Spring实战")
    private String categoryName;

    @Schema(description = "分类图书数量")
    private Long bookCount;

    public static CategoryVO toVO(Category category) {
        return new CategoryVO()
                   .setCategoryId(category.getCategoryId())
                   .setCategoryName(category.getCategoryName());
    }
}
