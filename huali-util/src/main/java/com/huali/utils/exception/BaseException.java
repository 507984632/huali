package com.huali.utils.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * @author myUser
 * @date 2021-01-21 16:22
 **/
public abstract class BaseException extends RuntimeException {
    protected Object data;

    public BaseException() {
    }

    public BaseException(Object data, String message) {
        super(message);
        this.data = data;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public abstract HttpStatus getHttpStatus();

    @Override
    public String getMessage() {
        return StringUtils.isBlank(super.getMessage()) ? getHttpStatus().getReasonPhrase() : super.getMessage();
    }

    public Object getData() {
        return data;
    }
}
