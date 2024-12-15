package com.readjoy.readjoyapi.controller.book;

import com.readjoy.readjoyapi.common.utils.Result;
import com.readjoy.readjoyapi.common.vo.resource.UserResourceVO;
import com.readjoy.readjoyapi.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
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
    Result<List<UserResourceVO>> getResourceListByPage(
            @RequestHeader(name = HEADER_NAME) String token,
            @PathVariable("bookId") Integer bookId
            ) {
        return Result.ok(resourceService.getResourceListByBooKId(bookId));
    }

}
