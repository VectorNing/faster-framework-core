package cn.faster.framework.core.exception;


import cn.faster.framework.core.exception.model.ErrorCode;

/**
 * @author zhangbowen 2018/5/29 12:08
 */
public class TokenValidException extends RuntimeException {
    private ErrorCode errorCode;

    public TokenValidException() {
    }

    public TokenValidException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}