package com.mvbbb.yim.gateway.handler;

import com.mvbbb.yim.common.exception.IMException;
import com.mvbbb.yim.common.protoc.http.ResCode;
import com.mvbbb.yim.common.protoc.http.response.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = IMException.class)
    public GenericResponse<Object> exception(IMException imException, HttpServletRequest request) {
        logger.error("出现异常。 url：{}，IMException：{}", request.getRequestURI(), imException.getResultCode());
        ResCode resultCode = imException.getResultCode();
        if (resultCode == null) {
            return GenericResponse.failed(imException.getMessage());
        }
        return GenericResponse.failed(resultCode);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public GenericResponse<Object> handlerRuntimeException(RuntimeException e, HttpServletRequest request) {
        e.printStackTrace();
        logger.error("服务器内部错误。url: {}", request.getRequestURI());
        return GenericResponse.failed(ResCode.SYSTEM_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

    }
}
