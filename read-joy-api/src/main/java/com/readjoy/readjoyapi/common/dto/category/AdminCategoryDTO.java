package com.readjoy.readjoyapi.common.dto.category;

import com.readjoy.readjoyapi.common.pojo.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * CategoryVO class.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCategoryDTO {

    @Schema(description = "书名", example = "Spring实战")
    @NotNull(message = "类别名称不能为空！")
    @Length(min = 1, max = 30, message = "类别名称长度必须在1-30之间！")
    private String categoryName;

    public static Category toEntity(AdminCategoryDTO adminCategoryDTO) {
        return new Category().setCategoryName(adminCategoryDTO.getCategoryName());
    }
    public static Category toEntity(AdminCategoryDTO adminCategoryDTO, Integer categoryId) {
        return new Category().setCategoryName(adminCategoryDTO.getCategoryName())
                .setCategoryId(categoryId);
    }

}
