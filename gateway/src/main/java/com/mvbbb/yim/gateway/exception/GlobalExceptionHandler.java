package com.mvbbb.yim.gateway.exception;

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
    public void exception(IMException imException, HttpServletRequest request){

    }

    @ExceptionHandler(value = RuntimeException.class)
    public void handlerRuntimeException(RuntimeException e, HttpServletRequest request){

    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){

    }
}
