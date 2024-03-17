package com.jiawei.filter;

import com.alibaba.fastjson.JSON;
import com.jiawei.domain.ResponseResult;
import com.jiawei.domain.entity.LoginUser;
import com.jiawei.enums.AppHttpCodeEnum;
import com.jiawei.utils.JwtUtil;
import com.jiawei.utils.RedisCache;
import com.jiawei.utils.WebUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    //自定义过滤器，并且在securityConfig中配置





    @Autowired
    private RedisCache redisCache;

    //验证登录状态
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头的token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)){
            //不带token
            //不需要登录直接放行
            filterChain.doFilter(request,response);
            return;
        }
        //解析获取token
        //带token时
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token 不合法时
            //响应告诉前端需要重新登录 401表示重新登录
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;

            //token非法
        }
        //token合法 且不超时时
        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);
        //如果获取不到
        if (Objects.isNull(loginUser)){
            //redis key过期了
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;
        }
        //存入到SecurityContext（）
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);//对请求放行

    }








}
