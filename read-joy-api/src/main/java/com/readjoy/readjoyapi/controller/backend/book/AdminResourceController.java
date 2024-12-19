package com.readjoy.readjoyapi.controller.backend.book;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.resource.InsertResourceDTO;
import com.readjoy.readjoyapi.common.dto.resource.SelectResourceDTO;
import com.readjoy.readjoyapi.common.dto.resource.UpdateResourceDTO;
import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.resource.AdminResourceVO;
import com.readjoy.readjoyapi.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 管理员模块/资源管理
 *
 * @className: ResourceController
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 16:30
 */
@RestController
@RequestMapping("/admin/book/resource")
@Tag("管理员模块/资源管理")
@Slf4j
public class AdminResourceController {

    @Resource
    private ResourceService resourceService;

    @Operation(description = "获取资源列表（分页）")
    @GetMapping("/page")
    Result<IPage<AdminResourceVO>> getResourceListByPage(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid SelectResourceDTO dto) {
        return Result.ok(resourceService.getPageByDTO(dto));
    }

    @Operation(description = "获取资源详情")
    @GetMapping("/{id}")
    Result<AdminResourceVO> getResourceDetail(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id) {
        return Result.ok(resourceService.getResourceDetail(id));
    }

    @Operation(description = "添加资源")
    @PostMapping
    Result<Integer> addResource(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid InsertResourceDTO dto) {
        return Result.ok(resourceService.addResource(dto));
    }

    @Operation(description = "修改资源")
    @PutMapping("/{id}")
    Result<Integer> updateResource(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id,
            @Valid UpdateResourceDTO dto) {
        return Result.ok(resourceService.updateResource(id, dto));
    }

    @Operation(description = "删除资源")
    @DeleteMapping("/{id}")
    Result<Integer> deleteResource(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id) {
        return Result.ok(resourceService.batchDeleteResource(new Integer[]{id}));
    }

    @Operation(description = "批量删除资源")
    @DeleteMapping("/batch")
    Result<Integer> batchDeleteResource(
            @RequestHeader(name = HEADER_NAME) String token,
            @RequestParam("ids") Integer[] ids
    ) {
        return Result.ok(resourceService.batchDeleteResource(ids));
    }

}
