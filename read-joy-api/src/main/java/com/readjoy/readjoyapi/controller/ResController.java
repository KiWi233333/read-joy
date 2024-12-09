package com.readjoy.readjoyapi.controller;

import com.readjoy.readjoyapi.common.utils.LocalFileUtil;
import com.readjoy.readjoyapi.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 资源模块
 */
@RestController
@RequestMapping("/res")
@Tag("资源模块")
@Slf4j
public class ResController {


    @Resource
    private LocalFileUtil localFileUtil;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<String> upload(
            @RequestHeader(name = HEADER_NAME) String token,
            @RequestParam("file") MultipartFile file
    ) {
        return Result.ok(localFileUtil.saveFile(file));
    }
}
