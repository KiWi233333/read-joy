package com.readjoy.readjoyapi.common.dto.category;

import com.readjoy.readjoyapi.common.dto.defaults.PageDTO;
import com.readjoy.readjoyapi.common.vo.category.CategoryVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SelectAdminCategoryDTO extends PageDTO<CategoryVO> {

    @Length(max = 100, message = "关键字不超过100字！")
    @Schema(description = "关键字", example = "科幻")
    private String keyword;
}
