package com.shuabao.apiServer.config;

import com.shuabao.apiServer.init.ParamsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

/**
 * Created by Scott Wei on 4/8/2018.
 */
//配置攔截器，靜態頁面位置,首頁
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer{

    @Autowired
    ParamsInterceptor paramsInterceptor;

    //攔截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(paramsInterceptor).excludePathPatterns("/user/verificationCode","/user/register","/user/login","/user/logout","/img/**","/");
    }

    //處理靜態資源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    //首頁
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    //配置上传数据大小
    @Bean
    public MultipartConfigElement multipartConfigElement () {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5MB");// 单个数据大小
        factory.setMaxRequestSize("10MB");// 总上传数据大小
        return factory.createMultipartConfig();
    }
}
