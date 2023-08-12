package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketiterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("LoginTicketiterceptor");
        //从cookie中获取凭证
        String ticket = CookieUtil.getValue(request,"ticket");
//        System.out.println("ticket is " + ticket);
        if(ticket != null)
        {
            //查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //判断是否有效
            if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date()))
            {
                User user = userService.findUserById(loginTicket.getUserId());
                //在本次请求中持有用户
//                if(user == null)
//                    System.out.println("pre user is null");
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    //在模板引擎调用之前将user存到model中
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
//        System.out.println("post handler: "+ user.toString());
        if(user != null && modelAndView != null)
        {
            modelAndView.addObject("loginUser",user);
        }
        else
        {
            if(user == null)
                System.out.println("user is null");
            else
                System.out.println("model is null");
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
