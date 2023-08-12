package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @RequestMapping(path="/login", method = RequestMethod.GET)
    public String getLoginPage()
    {
        return "/site/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path="/register", method = RequestMethod.POST)
    public String register(Model model, User user)
    {
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty())
        {
            model.addAttribute("msg","注册成功，成功发送邮件");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }
        model.addAttribute("usernameMsg",map.get("usernameMsg"));
        model.addAttribute("passwordMsg",map.get("passwordMsg"));
        model.addAttribute("emailMsg",map.get("emailMsg"));
        return "/site/register";
    }

    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功啦！");
            model.addAttribute("target", "/login");
        }
        if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "该账号已经被激活过了");
            model.addAttribute("target", "/index");
        }
        if (result == ACTIVATION_FAILURE) {
            model.addAttribute("msg", "激活失败，您的激活链接是无效的");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //生成验证码，不是string和网页，所以是void，需要我们自己手动向response中输出
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //保存验证码到session
        session.setAttribute("kaptcha", text);

        //将图片输出到浏览器
        response.setContentType("image/png");

        //需要解决验证码的归属问题
//        String kaptchaOwner = CommunityUtil.generateUUID();
//        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
//        cookie.setMaxAge(60);
//        cookie.setPath(contextPath);
//        response.addCookie(cookie);

        //将验证码存入redis
//        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
//        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);

        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败" + e.getMessage());
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberMe,
                        Model model, HttpSession session, HttpServletResponse response)
    {
        String kaptcha = (String)session.getAttribute("kaptcha");

        //判断验证码
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || kaptcha.equalsIgnoreCase(code) == false)
        {
            model.addAttribute("codeMsg","验证码错误");
            return "/site/login";
        }

        //检查账号密码
        int expiredSeconds = rememberMe? REMEMBER_EXPIRED_SECONDS: DEFALUT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket"))
        {
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }
        else
        {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }


    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";//重定向默认get请求
    }

}
