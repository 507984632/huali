package com.huali.utils.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author myUser
 * @date 2021-01-21 18:35
 **/
@Data
public class CheckedException extends BaseException {

    public CheckedException() {
    }

    public CheckedException(String message) {
        super(message);
    }

    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckedException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
