package com.readjoy.readjoyapi.common.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 管理员
 */
@Data
public class AdminDataDTO {

    @Schema(description = "开始时间", example = "2024-01-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateTime;

    @Schema(description = "结束时间", example = "2024-12-31 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDateTime;

    public String getRedisKey() {
        if (startDateTime == null || endDateTime == null) {
            return null;
        }
        return "admin_data_" + startDateTime.getTime() + "_" + endDateTime.getTime();
    }

}
