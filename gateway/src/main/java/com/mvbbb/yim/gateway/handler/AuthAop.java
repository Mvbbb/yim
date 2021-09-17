package com.mvbbb.yim.gateway.handler;

import com.mvbbb.yim.auth.service.AuthService;
import com.mvbbb.yim.common.exception.IMException;
import com.mvbbb.yim.common.protoc.AuthEnum;
import com.mvbbb.yim.common.protoc.http.request.GenericRequest;
import com.mvbbb.yim.gateway.CheckAuth;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuthAop {

    private static final Logger logger = LoggerFactory.getLogger(AuthAop.class);

    @DubboReference(check = false)
    AuthService authService;

    @Pointcut("@within(com.mvbbb.yim.gateway.CheckAuth) || @annotation(com.mvbbb.yim.gateway.CheckAuth)")
    public void pointCutDeclaration() {
    }

    @SneakyThrows
    @Before("pointCutDeclaration()")
    public void doAuth(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        // @ChechAuth 获取注解信息
        Class<?> aClass = joinPoint.getTarget().getClass();
        String targetName = aClass.getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        Method method = aClass.getMethod(methodName, parameterTypes);
        CheckAuth checkAuth = null;
        CheckAuth annotationOnType = aClass.getAnnotation(CheckAuth.class);
        CheckAuth annotationOnMethod = method.getAnnotation(CheckAuth.class);
        if (annotationOnMethod != null) {
            checkAuth = annotationOnMethod;
        } else {
            checkAuth = annotationOnType;
        }

        // @CheckAuth check=true 的情况下才需要鉴权
        if (checkAuth == null || !checkAuth.check()) {
            return;
        }

        if (args == null || args.length == 0) {
            throw new IMException("参数错误");
        }
        for (Object arg : args) {
            if (arg instanceof GenericRequest) {
                GenericRequest<?> request = (GenericRequest<?>) arg;
                String userId = request.getUserId();
                String token = request.getToken();

                if (token == null || userId == null) {
                    throw new IMException("需要提供认证信息");
                }
                AuthEnum authEnum = authService.checkToken(userId, token);
                if (authEnum == AuthEnum.WRONG_TOKEN) {
                    throw new IMException("token 错误");
                } else if (authEnum == AuthEnum.USER_NOT_EXIST) {
                    throw new IMException("用户不存在");
                } else if (authEnum == AuthEnum.SUCCESS) {
                    logger.info("通过身份验证。 userId：{}，token：{}", userId, token);
                    return;
                }
            }
        }
        throw new IMException("服务器错误");
    }


}

