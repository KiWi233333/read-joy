package com.readjoy.readjoyapi.controller.book;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.readjoy.readjoyapi.common.dto.resource.InsertResourceDTO;
import com.readjoy.readjoyapi.common.dto.resource.SelectResourceDTO;
import com.readjoy.readjoyapi.common.dto.resource.UpdateResourceDTO;
import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.resource.CuResourceVO;
import com.readjoy.readjoyapi.common.vo.resource.ResourceVO;
import com.readjoy.readjoyapi.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.readjoy.readjoyapi.common.utils.UserTokenUtil.HEADER_NAME;

/**
 * 资源模块
 *
 * @className: ResourceController
 * @author: Kiwi23333
 * @description: TODO描述
 * @date: 2024/12/7 16:30
 */
@RestController
@RequestMapping("/book/resource")
@Tag("资源模块")
@Slf4j
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @Operation(description = "获取图书的资源列表")
    @GetMapping("/list/{bookId}")
    Result<List<CuResourceVO>> getResourceListByPage(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("bookId") Integer bookId
            ) {
        return Result.ok(resourceService.getResourceListByBooKId(bookId));
    }

//    @Operation(description = "获取资源详情")
//    @GetMapping("/{id}")
//    Result<ResourceVO> getResourceDetail(
//            @RequestHeader(name = HEADER_NAME) String token,
//            @PathVariable("id") Integer id) {
//        return Result.ok(resourceService.getResourceDetail(id));
//    }

    @Operation(description = "行为操作资源(点赞/)")
    @PostMapping
    Result<Integer> addResource(
            @RequestHeader(name = HEADER_NAME) String token,
            @Valid @RequestBody InsertResourceDTO dto) {
        return Result.ok(resourceService.addResource(dto));
    }

    @Operation(description = "修改资源")
    @PutMapping("/{id}")
    Result<Integer> updateResource(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateResourceDTO dto) {
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
