package com.readjoy.readjoyapi.controller;

import com.readjoy.readjoyapi.common.enums.SysUserTypeEnum;
import com.readjoy.readjoyapi.common.utils.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 资源模块
 */
@RequestMapping("")
@Controller
@Tag("资源模块")
@Slf4j
public class ResController {


    @Resource
    private LocalFileUtil localFileUtil;

    @PostMapping("/res/upload")
    @ResponseBody
    @Operation(summary = "上传文件")
    public Result<String> upload(
            @RequestHeader(name = HEADER_NAME) String token,
            @RequestParam("file") MultipartFile file
    ) {
        return Result.ok(localFileUtil.saveFile(file));
    }

    @PostMapping("/res/upload/auth")
    @ResponseBody
    @Operation(summary = "上传文件(权限文件)")
    public Result<String> uploadAuth(
            @RequestHeader(name = HEADER_NAME) String token,
            @RequestParam("file") MultipartFile file
    ) {
        final UserTokenUtil tokenUtil = RequestHolderUtil.get();
        AssertUtil.isTrue(tokenUtil != null && SysUserTypeEnum.isAdmin(tokenUtil.getUserType()), "抱歉，您没有权限访问该接口！");
        return Result.ok(localFileUtil.saveAuthFile(file));
    }

}
