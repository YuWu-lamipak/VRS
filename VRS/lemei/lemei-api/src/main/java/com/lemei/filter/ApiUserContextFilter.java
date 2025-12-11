package com.lemei.filter;

import com.lemei.common.core.domain.model.LoginUser;
import com.lemei.common.utils.StringUtils;
import com.lemei.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * API用户上下文过滤器
 * 用于在禁用Security的情况下维护用户会话隔离
 * 
 * 解决问题：
 * 1. 多用户同时访问时的用户ID混乱问题
 * 2. 线程安全问题
 * 3. 用户会话隔离
 */
public class ApiUserContextFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String threadName = Thread.currentThread().getName();
        
        System.out.println("=== API请求开始 ===");
        System.out.println("请求URI: " + requestURI);
        System.out.println("请求方法: " + method);
        System.out.println("线程名称: " + threadName);
        
        try {
            // 尝试从请求中获取用户信息
            LoginUser loginUser = tokenService.getLoginUser(request);
            
            if (StringUtils.isNotNull(loginUser)) {
                System.out.println("成功获取用户信息 - 用户ID: " + loginUser.getUserId() + 
                                 ", 用户名: " + loginUser.getUsername());
                
                // 设置用户上下文，确保每个请求都有独立的用户信息
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                System.out.println("用户上下文设置成功");
            } else {
                System.out.println("未获取到用户信息，可能是匿名访问或token无效");
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            System.err.println("获取用户信息异常: " + e.getMessage());
            // 如果获取用户信息失败，清空上下文
            SecurityContextHolder.clearContext();
        }
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 请求结束后清空上下文，防止线程污染
            SecurityContextHolder.clearContext();
            System.out.println("=== API请求结束，上下文已清理 ===");
        }
    }
}