package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;

import com.nowcoder.community.entity.Event;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    //为了得到当前用户的id等信息，注入holder
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private EventProducer eventProducer;


    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        //统一的异常处理（如果空）
        //统一权限认证
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //触发评论事件
        //触发评论
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setUserId(hostHolder.getUser().getId())
                .setData("postId", discussPostId);//方便跳转到帖子位置
        //不同类型分别获取作者
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost post = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(post.getUserId());
        } else {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

//        if (comment.getEntityType() == ENTITY_TYPE_POST) {
//            event = new Event()
//                    .setTopic(TOPIC_PUBLISH)
//                    .setEntityType(comment.getEntityType())
//                    .setEntityId(discussPostId)
//                    .setUserId(comment.getUserId());
//            eventProducer.fireEvent(event);
//            String redisKey = RedisKeyUtil.getPostScore();
//            redisTemplate.opsForSet().add(redisKey, discussPostId);
//        }

        return "redirect:/discuss/detail/" + discussPostId;
    }
}

