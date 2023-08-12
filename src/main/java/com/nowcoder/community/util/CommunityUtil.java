package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
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
}
