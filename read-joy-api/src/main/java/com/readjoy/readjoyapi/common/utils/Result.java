package com.readjoy.readjoyapi.common.utils;

import com.readjoy.readjoyapi.common.enums.ResultStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 接口返回结果模板
 *
 * @className: Result
 * @author: Kiwi2333
 * @description: 接口返回模板
 * @date: 2024/12/4 21:35
 */
@Data
@AllArgsConstructor
public class Result<T> {


    /**
     * @see ResultStatus
     */
    @Schema(title = "状态码", example = "20000", description = """
            20000: 操作成功
            20001: 添加或已经存在
            20002: 被删或不存在
            20003: 查询不到
            20004: 修改失败
            20005: 链接元素不存在
            40001: 空值错误
            40002: 认证失败
            40003: 参数错误
            40004: 阻塞或被占用繁忙
            40005: 操作错误，请重试！
            40006: 权限不足
            40007: 身份过期，请重新登录！
            40008: 账号被封禁，详情联系客服！
            40009: 认证失败，登录设备有误！
            """)
    private Integer code;
    @Schema(description = "描述信息")
    private String message;
    @Schema(description = "返回数据")
    private T data;


    /**
     * 成功
     **/
    public static Result<Object> ok() {
        return new Result<>(ResultStatus.SUCCESS.getCode(), "操作成功!", null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultStatus.SUCCESS.getCode(), "操作成功!", data);
    }

    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(ResultStatus.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败
     **/
    public static Result<Object> fail() {
        return new Result<>(ResultStatus.NULL_ERR.getCode(), "操作失败！", null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultStatus.NULL_ERR.getCode(), message, null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> fail(ResultStatus resultStatus, String message) {
        return new Result<>(resultStatus.getCode(), message, null);
    }

    public static <T> Result<T> fail(ResultStatus status) {
        return new Result<>(status.getCode(), status.getMessage(), null);
    }

    public static <T> Result<T> fail(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

}
