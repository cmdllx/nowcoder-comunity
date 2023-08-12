package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: CookieUtil
 * Package: com.nowcoder.community.util
 * Description:
 *
 * @Author CC
 * @Create 2023/8/11 17:16
 * @Version 1.0
 */
public class CookieUtil {
    public static String getValue(HttpServletRequest request, String name)
    {
        if(request == null || name == null)
        {
            throw new IllegalArgumentException("参数为空");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies)
            {
                if(cookie.getName().equals(name))
                {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
