package com.readjoy.readjoyapi.common.dto.book;

import com.readjoy.readjoyapi.common.dto.defaults.PageDTO;
import com.readjoy.readjoyapi.common.vo.book.BookVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;


@Data
@Accessors(chain = true)
public class SelectBookDTO extends PageDTO<BookVO> {

    @Length(max = 100, message = "关键字不超过100字！")
    @Schema(description = "关键字", example = "java")
    private String keyword;

    @Schema(description = "分类id", example = "1")
    private Integer categoryId;

}
