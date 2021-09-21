package com.mvbbb.yim.gateway.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
public class HttpRequestLogger extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestLogger.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String userId = request.getHeader("userId");
        String token = request.getHeader("token");
        ServletRequest servletRequest = new ContentCachingRequestWrapper(request);
        Map<String, String[]> params = servletRequest.getParameterMap();
        StringBuffer stringBuffer = new StringBuffer();
        params.forEach((key, value) -> {
            stringBuffer.append(key).append(":").append(value[0]).append("|");
        });
        logger.info("==>> RequestURI:{}, userId:{}, token:{}, params: {}", requestURI, userId, token, stringBuffer);

        return true;
    }
}
