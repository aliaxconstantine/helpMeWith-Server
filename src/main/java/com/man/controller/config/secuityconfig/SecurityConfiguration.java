package com.man.controller.config.secuityconfig;
import com.man.controller.config.secuityconfig.secuityEvent.CAuthenticationFailureHandler;
import com.man.controller.config.secuityconfig.secuityEvent.CAuthenticationSuccessHandler;
import com.man.config.secuityconfig.secuityfliter.*;
import com.man.controller.config.secuityconfig.secuityfliter.*;
import com.man.service.impl.Security.UserServiceImpl;
import com.man.utils.SystemConstants;
import jakarta.servlet.Filter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;

/**
 * @author 艾莉希雅
 */
@EnableWebSecurity
@Configuration
@Log4j2
public class SecurityConfiguration {

    private final CAuthenticationSuccessHandler cAuthenticationSuccessHandler;
    private final CAuthenticationFailureHandler cAuthenticationFailureHandler;
    private final UserServiceImpl userService;
    private final JsCodeAuthenticationProviderWithPhone jsCodeAuthenticationProviderWithPhone;
    private final JsCodeAuthenticationProvider jsCodeAuthenticationProvider;
    private final StringRedisTemplate stringRedisTemplate;
    @Autowired
    public SecurityConfiguration(
            CAuthenticationSuccessHandler cAuthenticationSuccessHandler,
            CAuthenticationFailureHandler cAuthenticationFailureHandler,
            UserServiceImpl userService,
            JsCodeAuthenticationProviderWithPhone jsCodeAuthenticationProviderWithPhone,
            JsCodeAuthenticationProvider jsCodeAuthenticationProvider, StringRedisTemplate stringRedisTemplate){
        this.cAuthenticationSuccessHandler = cAuthenticationSuccessHandler;
        this.cAuthenticationFailureHandler = cAuthenticationFailureHandler;
        this.userService = userService;
        this.jsCodeAuthenticationProviderWithPhone = jsCodeAuthenticationProviderWithPhone;
        this.jsCodeAuthenticationProvider = jsCodeAuthenticationProvider;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // DaoAuthenticationProvider 从自定义的 userDetailsService.loadUserByUsername 方法获取UserDetails
        authProvider.setUserDetailsService(userDetailsService());
        // 设置密码编辑器
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService() {return userService;}

    @Bean
    public Filter getUsePasswordFilter(){
        UserLoginWithPasswordFilter usernamePasswordAuthenticationFilter = new UserLoginWithPasswordFilter(stringRedisTemplate);
        usernamePasswordAuthenticationFilter.setPasswordParameter("psd");
        usernamePasswordAuthenticationFilter.setUsernameParameter("name");
        usernamePasswordAuthenticationFilter.setAuthenticationManager(new ProviderManager(jsCodeAuthenticationProvider));
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(cAuthenticationSuccessHandler);
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(cAuthenticationFailureHandler);
        usernamePasswordAuthenticationFilter.setFilterProcessesUrl("/userLogin");
        return usernamePasswordAuthenticationFilter;
    }
    @Bean
    public Filter getPhoneFilter(){
        UserLoginWithPhoneFilter usernamePasswordAuthenticationFilter = new UserLoginWithPhoneFilter(stringRedisTemplate);
        usernamePasswordAuthenticationFilter.setPasswordParameter("code");
        usernamePasswordAuthenticationFilter.setUsernameParameter("phone");
        usernamePasswordAuthenticationFilter.setAuthenticationManager(new ProviderManager(jsCodeAuthenticationProviderWithPhone));
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(cAuthenticationSuccessHandler);
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(cAuthenticationFailureHandler);
        usernamePasswordAuthenticationFilter.setFilterProcessesUrl("/phoneLogin");
        return usernamePasswordAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authenticationProvider(authenticationProvider())
                .authorizeHttpRequests((auths) -> auths
                        .dispatcherTypeMatchers(FORWARD, ERROR).permitAll()
                        // 允许所有人访问的url
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(SystemConstants.publicUrls).permitAll()
                        //访问资源需要认证
                        .requestMatchers("/user").hasAuthority("/user")
                        .requestMatchers("/task").hasAuthority("/task")
                        .requestMatchers("/upload").hasAuthority("/upload")
                        .requestMatchers("/communication").hasAuthority("/communication")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                //只能单用户登录
                .sessionManagement(session -> session.maximumSessions(1))
                .formLogin(Customizer.withDefaults());

        httpSecurity.addFilterBefore(new JETCheckFilter(),UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(getUsePasswordFilter(),UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(getPhoneFilter(),UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> {
            web.ignoring().requestMatchers(SystemConstants.publicUrls);
        });
    }
}
