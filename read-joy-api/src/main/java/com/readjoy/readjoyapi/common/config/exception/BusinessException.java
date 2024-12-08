package com.readjoy.readjoyapi.common.config.exception;

import com.readjoy.readjoyapi.common.enums.ResultStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 业务错误类
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected Integer errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;
    /**
     * 错误数据
     */
    protected Object data;

    public BusinessException() {
        super();
    }

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorCode = ResultStatus.DEFAULT_ERR.getCode();
        this.errorMsg = errorMsg;
    }

    public BusinessException(ResultStatus status) {
        super(status.getMessage());
        this.errorCode = status.getCode();
        this.errorMsg = status.getMessage();
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    public BusinessException(ResultStatus errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorMsg;
    }
    public BusinessException(Integer errorCode, String errorMsg, Object data) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public BusinessException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage() {
        return errorMsg;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
