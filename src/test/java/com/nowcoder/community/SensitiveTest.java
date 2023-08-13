package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * ClassName: SensitiveTest
 * Package: com.nowcoder.community
 * Description:
 *
 * @Author CC
 * @Create 2023/8/13 19:59
 * @Version 1.0
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter()
    {
        String text = "hahaha赌★博，嫖娼，吸毒，开票";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
