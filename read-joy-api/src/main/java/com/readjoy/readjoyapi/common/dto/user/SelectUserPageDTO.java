package com.readjoy.readjoyapi.common.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.readjoy.readjoyapi.common.dto.defaults.PageDTO;
import com.readjoy.readjoyapi.common.vo.book.BookVO;
import com.readjoy.readjoyapi.common.vo.user.UserInfoVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SelectUserPageDTO extends PageDTO<UserInfoVO> {

    @Length(max = 100, message = "关键字不超过100字！")
    @Schema(description = "关键字", example = "java")
    private String keyword;

    @Schema(description = "创建排序 1-降序 desc 2-升序 asc")
    @Range(min = 1, max = 2, message = "排序方式只能为1或2！")
    private Integer createOrder;

    @Schema(description = "是否已审核 0-未审核 1-已审核")
    @Range(min = 0, max = 1, message = "审核状态只能为0或1！")
    private Integer isChecked;

    public boolean checkOrderDesc() {
        return createOrder != null && createOrder == 1;
    }

    public boolean checkOrderAsc() {
        return createOrder != null && createOrder == 2;
    }
}
