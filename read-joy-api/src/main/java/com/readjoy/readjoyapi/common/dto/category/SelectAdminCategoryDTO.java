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

    @Schema(description = "按id排序：1-降序，2-升序", example = "1")
    private Integer idSort = 1;


    public boolean checkDesc() {
        return idSort != null && idSort == 1;
    }

    public boolean checkAsc() {
        return idSort != null && idSort == 2;
    }
}
