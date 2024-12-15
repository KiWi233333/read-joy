package com.readjoy.readjoyapi.controller.backend;

import com.readjoy.readjoyapi.common.utils.AssertUtil;
import com.readjoy.readjoyapi.common.utils.LocalFileUtil;
import com.readjoy.readjoyapi.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 管理员模块/公共模块
 */
@RestController
@RequestMapping("/admin/res")
@Tag("管理员模块/公共模块")
@Slf4j
public class AdminResController {


    @Resource
    private LocalFileUtil localFileUtil;

    @PostMapping("/file/auth")
    @Operation(summary = "上传文件(权限文件)")
    public Result<String> uploadAuth(
            @RequestHeader(name = HEADER_NAME) String token,
            @RequestParam("file") MultipartFile file
    ) {
        return Result.ok(localFileUtil.saveAuthFile(file));
    }

    @DeleteMapping("/file/auth")
    @Operation(summary = "删除文件(权限文件)")
    public Result<Boolean> deleteAuth(
            @RequestHeader(name = HEADER_NAME) String token,
            @RequestParam("url") String url
    ) {
        AssertUtil.isNotBlank(url, "url不能为空！");
        return Result.ok(localFileUtil.deleteAuthFile(url));
    }

}
