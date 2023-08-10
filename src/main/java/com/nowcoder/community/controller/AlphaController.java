package com.nowcoder.community.controller;

import com.nowcoder.community.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ClassName: AlphaController
 * Package: com.newcoder.community.controller
 * Description:
 *
 * @Author CC
 * @Create 2023/8/1 19:46
 * @Version 1.0
 */
@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello()
    {
        return "Hello, spring boot";
    }

    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response)
    {
        //创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        //设置cookie生效范围
        cookie.setPath("/community/alpha");
        //设置cookie生存时间(s)
        cookie.setMaxAge(60 * 10);
        //发送cookie
        response.addCookie(cookie);
        return "set cookie";
    }

    @RequestMapping(path = "cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookie";
    }

    //session示例
    @RequestMapping(path = "session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session) {
        //SpringMVC会自动地注入，只需要声明即可
        session.setAttribute("id", 1);
        session.setAttribute("name", "test");
        return "set session";
    }

    @RequestMapping(path = "session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getId());
        System.out.println(session.getAttribute("name"));
        return "get session";
    }
}
