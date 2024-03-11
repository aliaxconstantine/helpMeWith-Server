package com.man.config.appConfig;

import com.man.Logger.CORSInterceptor;
import com.man.Logger.RequestLogFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Log4j2
public class AppConfig implements WebMvcConfigurer {
    private final CORSInterceptor corsInterceptor;
    private final RequestLogFilter requestLogFilter;
    @Autowired
    public AppConfig(CORSInterceptor corsInterceptor, RequestLogFilter requestLogFilter) {
        this.corsInterceptor = corsInterceptor;
        this.requestLogFilter = requestLogFilter;
    }
    @Bean
    public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new OrderedHiddenHttpMethodFilter();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("配置跨域拦截器");
        registry.addInterceptor(corsInterceptor)
                .addPathPatterns("/**");
        log.debug("配置日志记录拦截器");
        registry.addInterceptor(requestLogFilter)
                .addPathPatterns("/**");
    }
}
