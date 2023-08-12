package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * ClassName: LoginRequiredInterceptor
 * Package: com.nowcoder.community.controller.interceptor
 * Description:
 *
 * @Author CC
 * @Create 2023/8/12 20:37
 * @Version 1.0
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginRequiredInterceptor.class);
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //只拦截方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired != null && hostHolder.getUser() == null) {
                try {
                    //这个类是有接口声明的，不能像controller直接return一个地址，需要重定向（controller的底层也是重定向）
                    response.sendRedirect(request.getContextPath() + "/login");
                } catch (IOException e) {
                    logger.error("重定向登录页面失败" + e.getMessage());
                }
                return false;
            }
        }
        return true;
    }
}
