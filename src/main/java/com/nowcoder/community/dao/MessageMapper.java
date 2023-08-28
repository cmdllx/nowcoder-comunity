package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: MessageMapper
 * Package: com.nowcoder.community.dao
 * Description:
 *
 * @Author CC
 * @Create 2023/8/15 15:27
 * @Version 1.0
 */
@Mapper
public interface MessageMapper {
    //查询会话列表一页数据，每个会话，只返回一条最新的私信
    List<Message> selectConversations(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    //查询会话列表总行数
    int selectConversationCount(@Param("userId") int userId);

    //查询某个会话所包含的私信列表
    List<Message> selectLetters(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    //查询某个会话所包含的私信数量
    int selectLetterCount(@Param("conversationId") String conversationId);

    //查询未读消息数量
    int selectLetterUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    //新增消息
    int insertMessage(Message message);

    //修改状态
    int updateStatus(@Param("ids") List<Integer> ids, @Param("status") int status);

    //系统通知功能

    //查询某个主题下的最新通知（最新通知）
    Message selectLatestNotice(@Param("userId") int userId, @Param("topic") String topic);

    //某个主题包含的通知数量（显示数量）
    int selectNoticeCount(@Param("userId") int userId, @Param("topic") String topic);

    //查询未读通知数量
    //如果不穿topic，则查所有
    int selectNoticeUnreadCount(@Param("userId") int userId, @Param("topic") String topic);

    //查询某个主题的所有通知
    List<Message> selectNotices(@Param("userId") int userId, @Param("topic") String topic, @Param("offset") int offset, @Param("limit") int limit);

}
