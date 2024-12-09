package com.readjoy.readjoyapi.controller.backend.admin;

import com.readjoy.readjoyapi.common.dto.admin.AdminLoginDTO;
import com.readjoy.readjoyapi.common.dto.admin.AdminUpdatePwdDTO;
import com.readjoy.readjoyapi.common.service.AdminService;
import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.admin.AdminLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 管理员模块/登录注册
 *
 * @className: AdminController
 * @author: Kiwi23333
 * @description: 管理员模块/登录注册
 * @date: 2024/12/7 16:41
 */

@RestController
@RequestMapping("/admin")
@Tag("管理员模块/登录注册")
@Slf4j
public class AdminController {


    @Resource
    private AdminService adminService;

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    Result<AdminLoginVO> login(@Valid @RequestBody AdminLoginDTO loginDTO) {
        return Result.ok(adminService.login(loginDTO));
    }

    @PutMapping("/pwd")
    @Operation(summary = "修改密码")
    @Parameter(name = HEADER_NAME, in = ParameterIn.HEADER, description = "token", required = true)
    public Result<Integer> updatePwd(@Valid @RequestBody AdminUpdatePwdDTO dto, @RequestHeader(name = HEADER_NAME) String token) {
        return Result.ok(adminService.updatePwd(dto));
    }
}
