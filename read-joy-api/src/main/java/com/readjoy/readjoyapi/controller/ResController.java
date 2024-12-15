package com.readjoy.readjoyapi.controller;

import com.readjoy.readjoyapi.common.utils.AssertUtil;
import com.readjoy.readjoyapi.common.utils.LocalFileUtil;
import com.readjoy.readjoyapi.common.utils.RequestHolderUtil;
import com.readjoy.readjoyapi.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 公共模块
 */
@RequestMapping("/res")
@RestController
@Tag("公共模块")
@Slf4j
public class ResController {


    @Resource
    private LocalFileUtil localFileUtil;

    @PostMapping("/file")
    @Operation(summary = "上传文件")
    public Result<String> upload(
            @RequestHeader(name = HEADER_NAME) String token,
            @RequestParam("file") MultipartFile file
    ) {
        Integer userId = RequestHolderUtil.get().getId();
        return Result.ok(localFileUtil.saveFile(file, String.valueOf(userId)));
    }

    // 删除文件
    @DeleteMapping("/file")
    @Operation(summary = "删除文件")
    public Result<Boolean> delete(
            @RequestHeader(name = HEADER_NAME) String token,
            @Schema(description = "文件路径", example = "files/{uid}/xx/xx.xx") @RequestParam("url") String url
    ) {
        AssertUtil.isNotBlank(url, "文件名不能为空！");
        Integer userId = RequestHolderUtil.get().getId();
        boolean isSelfFile = url.startsWith("files/public/" + userId + "_");
        AssertUtil.isTrue(isSelfFile, "权限错误，不能操作非本人文件！");
        return Result.ok(localFileUtil.deleteFile(url));
    }

}
