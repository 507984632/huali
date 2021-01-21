package com.huali.utils.base;

import com.huali.utils.exception.BaseException;
import com.huali.utils.web.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

/**
 * @author myUser
 * @date 2021-01-21 15:21
 **/
@Slf4j
public abstract class BaseController {

    protected ResponseEntity<JsonResult<?>> success() {
        return ResponseEntity.ok(new JsonResult<>().setMessage(JsonResult.SUCCESS_MSG));
    }

    protected <T> ResponseEntity<JsonResult<T>> success(T data) {
        return ResponseEntity.ok(new JsonResult<>(data).setMessage(JsonResult.SUCCESS_MSG));
    }

    /**
     * 处理基础定义异常信息
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<JsonResult<Object>> baseExceptionHandler(BaseException exception) {
        log.error(exception.getMessage());
        log.error(Arrays.toString(exception.getStackTrace()));
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new JsonResult<>().setData(exception.getData()).setMessage(exception.getMessage()));
    }

    /**
     * 未知的异常捕获处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<JsonResult<String>> allUnknownExceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);
        Throwable e = exception;
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new JsonResult<String>().setMessage(e.getMessage()));
    }

}
