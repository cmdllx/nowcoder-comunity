package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;


@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostService.findDiscussPosts(123456, 0, 10);
        for (DiscussPost discussPost : list)
            System.out.println(discussPost);
        int rows = discussPostService.findDiscussPostsRows(123456);
        System.out.println(rows);
    }
}
