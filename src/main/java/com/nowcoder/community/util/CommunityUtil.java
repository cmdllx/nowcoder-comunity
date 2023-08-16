package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {

    //generate random string
    public static String generateUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //MD5 jiami
    public static String md5(String key)
    {
        if(StringUtils.isAllBlank(key))
        {
            return null;
        }
        else
        {
            return DigestUtils.md5DigestAsHex(key.getBytes());
        }
    }

    //整合发送给浏览器的json数据
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        //code肯定有，剩下两个不一定有
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    //重载
    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    //这个类比较简单，就不用bean注入测试了，直接main测试
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangtie");
        map.put("age", 24);
        System.out.println(getJSONString(0, "ok", map));
    }


}
