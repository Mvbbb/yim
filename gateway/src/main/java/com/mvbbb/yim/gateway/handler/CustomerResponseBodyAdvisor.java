package com.mvbbb.yim.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 打印响应值日志
 */
@ControllerAdvice
public class CustomerResponseBodyAdvisor implements ResponseBodyAdvice<Object> {
    private static final Logger logger = LoggerFactory.getLogger(CustomerResponseBodyAdvisor.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // 响应值转JSON串输出到日志系统
        if (logger.isInfoEnabled()) {
            logger.info("<<== {}: {}", request.getURI(), JSON.toJSONString(body, SerializerFeature.UseSingleQuotes));
        }
        return body;
    }

}