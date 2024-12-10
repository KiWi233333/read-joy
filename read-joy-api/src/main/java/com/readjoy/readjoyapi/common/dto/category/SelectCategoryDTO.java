package com.readjoy.readjoyapi.common.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;


@Data
@Accessors(chain = true)
public class SelectCategoryDTO  {

    @Length(max = 100, message = "关键字不超过100字！")
    @Schema(description = "关键字", example = "科幻")
    private String keyword;
}
