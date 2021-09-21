package com.mvbbb.yim.gateway.handler;

import com.mvbbb.yim.auth.service.AuthService;
import com.mvbbb.yim.common.exception.IMException;
import com.mvbbb.yim.common.protoc.AuthEnum;
import com.mvbbb.yim.gateway.CheckAuth;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    @DubboReference(check = false)
    AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        CheckAuth methodCheckAuthAnnotation = method.getAnnotation(CheckAuth.class);
        CheckAuth classCheckAuthAnnotation = handlerMethod.getMethod().getDeclaringClass().getAnnotation(CheckAuth.class);

        if (methodCheckAuthAnnotation != null) {
            if (methodCheckAuthAnnotation.check()) {
                return doAuth(request, response);
            } else {
                return true;
            }
        }
        if (classCheckAuthAnnotation != null && classCheckAuthAnnotation.check()) {
            return doAuth(request, response);
        }
        return true;
    }

    private boolean doAuth(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getHeader("userId");
        String token = request.getHeader("token");

        if (userId == null || token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new IMException("缺少认证信息");
        }

        AuthEnum authEnum = authService.checkToken(userId, token);
        if (authEnum == AuthEnum.WRONG_TOKEN) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new IMException("token 错误");
        } else if (authEnum == AuthEnum.USER_NOT_EXIST) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new IMException("用户不存在");
        } else if (authEnum == AuthEnum.SUCCESS) {
            logger.info("通过身份验证。 userId：{}，token：{}", userId, token);
            return true;
        }
        return false;
    }
}
