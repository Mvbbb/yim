package com.mvbbb.yim.gateway.config;

import com.mvbbb.yim.gateway.handler.AuthorizationInterceptor;
import com.mvbbb.yim.gateway.handler.HttpRequestLogger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

@Configuration
public class InterceptorRegister extends WebMvcConfigurationSupport {

    @Resource
    AuthorizationInterceptor authorizationInterceptor;
    @Resource
    HttpRequestLogger httpRequestLogger;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpRequestLogger);
        registry.addInterceptor(authorizationInterceptor);
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将templates目录下的CSS、JS文件映射为静态资源，防止Spring把这些资源识别成thymeleaf模版
        registry.addResourceHandler("/templates/**.js").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/templates/**.css").addResourceLocations("classpath:/templates/");
        //其他静态资源
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        //swagger增加url映射
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
