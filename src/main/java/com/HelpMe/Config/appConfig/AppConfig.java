package com.HelpMe.Config.appConfig;
import com.HelpMe.Logger.RequestLogFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author 艾莉希雅
 */
@Configuration
@Log4j2
public class AppConfig implements WebMvcConfigurer {
    private final RequestLogFilter requestLogFilter;

    @Autowired
    public AppConfig(RequestLogFilter requestFilter) {
       this.requestLogFilter = requestFilter;
    }
    @Bean
    public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new OrderedHiddenHttpMethodFilter();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogFilter)
                .addPathPatterns("/**");
    }
}
