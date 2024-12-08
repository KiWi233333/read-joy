package com.readjoy.readjoyapi.controller;

import com.readjoy.readjoyapi.common.utils.LocalFileUtil;
import com.readjoy.readjoyapi.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 资源模块
 */
@Controller
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
            @RequestParam("uploadFile") MultipartFile uploadFile
    ) {
        return Result.ok(localFileUtil.saveFile(uploadFile));
    }
    @GetMapping("/file/**")
    @Operation(summary = "文件下载(访问)")
    public String download(HttpServletRequest request) {
        return request.getRequestURI().replace("/res/file/", "");
    }
}
