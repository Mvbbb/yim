package com.mvbbb.yim.gateway.handler;

// CustomerRequestBodyAdvisor.java

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 打印请求参数日志
 */
@Order(0)
@ControllerAdvice
public class CustomerRequestBodyAdvisor extends RequestBodyAdviceAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRequestBodyAdvisor.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = parameter.getMethod();

        // 参数对象转JSON字符串
        String jsonBody;
        if (StringHttpMessageConverter.class.isAssignableFrom(converterType)) {
            jsonBody = body.toString();
        } else {
            jsonBody = JSON.toJSONString(body, SerializerFeature.UseSingleQuotes);
        }

        // 自定义日志输出
        if (logger.isInfoEnabled()) {
            logger.info("==>> {}#{}: {}", parameter.getContainingClass().getSimpleName(), method.getName(), jsonBody);
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}