package net.kassin.javaspringexamples.stats.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ChannelTopic channelTopic = new ChannelTopic("stats-invalidation-channel");

    public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publishInvalidation(UUID uuid){
        redisTemplate.convertAndSend(channelTopic.getTopic(),uuid.toString());
        System.out.println("PUB/SUB: Invalidação publicada para: " + uuid);
    }

}
