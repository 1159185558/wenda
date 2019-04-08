package com.example.wenda.configuration;

import com.example.wenda.interceptor.LoginRequiredInterceptor;
import com.example.wenda.interceptor.PassportedInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: weilei
 * @CreateTime: 2019/3/27  16:48
 * @Description:
 */

@Configuration
public class WenDaWebConfiguration implements WebMvcConfigurer {
    @Autowired
    PassportedInterceptor passportedInterceptor;
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*registry.addInterceptor(passportedInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
        WebMvcConfigurer.super.addInterceptors(registry);*/
    }
}
