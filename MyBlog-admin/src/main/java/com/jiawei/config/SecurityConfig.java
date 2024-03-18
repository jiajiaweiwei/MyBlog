package com.jiawei.config;


import com.jiawei.filter.JwtAuthenticationTokenFilter;
import com.jiawei.hander.security.AccessDeniedHandlerImpl;
import com.jiawei.hander.security.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    //注入容器，供BlogLoginServiceImpl使用
    @Autowired
    private UserDetailsService userDetailsService;
    //注入过滤器
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    //注入认证失败和授权失败处理器
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;




    //重写该方法暴露 AuthenticationManager对象
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        //关联加密编码器
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        //将daoAuthenticationProvider放进ProviderManager中，包含进去
        return new ProviderManager(daoAuthenticationProvider);
    }

    //密码加密
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        //对于登录接口哦，允许匿名访问，其他接口都不需要认证即可访问
        http.authorizeHttpRequests().requestMatchers("/user/login").anonymous()
                        .anyRequest().authenticated();


        //关闭默认的注销功能接口
        http.logout().disable();
        //允许跨域
        http.cors();
        //配置自定义过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter,UsernamePasswordAuthenticationFilter.class);
        //配置异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);



        return http.build();


    }






}
