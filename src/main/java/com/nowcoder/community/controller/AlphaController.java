package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
