package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * ClassName: LikeService
 * Package: com.nowcoder.community.service
 * Description:
 *
 * @Author CC
 * @Create 2023/8/23 14:29
 * @Version 1.0
 */
@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId, int entityType, int entityId) {
        //这部分只是更新了点赞功能，但是缺乏统计功能
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (isMember) {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
        //更新某用户的赞，需要保证事务性
//        redisTemplate.execute(new SessionCallback() {
//            @Override
//            public Object execute(RedisOperations operations) throws DataAccessException {
//                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
//                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);
//                //开启事务
//                operations.multi();
//                if (isMember) {
//                    operations.opsForSet().remove(entityLikeKey, userId);
//                    operations.opsForValue().decrement(userLikeKey);
//                } else {
//                    operations.opsForSet().add(entityLikeKey, userId);
//                    operations.opsForValue().increment(userLikeKey);
//                }
//                return operations.exec();
//            }
//        });
    }

    //查询实体点赞数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体是否点过赞(int更具扩展性，万一将来引入了点踩功能）
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    //查询某用户获得的赞数量
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count;
    }
}
