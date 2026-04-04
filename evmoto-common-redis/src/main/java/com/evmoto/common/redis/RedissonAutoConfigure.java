package com.evmoto.common.redis;

import com.evmoto.common.redis.client.*;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties(RedissonProperties.class)
@ConditionalOnProperty(name = RedissonConstants.REDISSON_ENABLE)
public class RedissonAutoConfigure {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonAutoConfigure.class);

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(RedissonProperties redissonProperties) {
        LOGGER.info(">>>>>>>>>>> redisson config init.");
        return RedissonBuilder.build(redissonProperties);
    }

    @Bean
    @ConditionalOnMissingBean(RedissonConstants.class)
    public RedissonConstants redissonConstants() {
        return new RedissonConstants();
    }

    /*@Bean
    @ConditionalOnMissingBean(RedissonBucketClient.class)
    public RedissonBucketClient redissonBucketClient() {
        return new RedissonBucketClient();
    }

    @Bean
    @ConditionalOnMissingBean(RedissonMapClient.class)
    public RedissonMapClient redissonMapClient() {
        return new RedissonMapClient();
    }*/

    @Bean
    @ConditionalOnMissingBean(RedissonLockClient.class)
    public RedissonLockClient redissonLockClient() {
        return new RedissonLockClient();
    }


    @Bean
    @ConditionalOnMissingBean(EvmotoRedisClient.class)
    public EvmotoRedisClient evmotoRedisClient(RedissonClient redissonClient) {
        return new EvmotoRedisClient(redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean(ShareRedisClient.class)
    public ShareRedisClient shareRedisClient(EvmotoRedisClient evmotoRedisClient) {
        return new ShareRedisClient(evmotoRedisClient);
    }

    @Bean
    @ConditionalOnMissingBean(OrderRedisClient.class)
    public OrderRedisClient orderRedisClient(EvmotoRedisClient evmotoRedisClient) {
        return new OrderRedisClient(evmotoRedisClient);
    }



    /*@Bean
    @ConditionalOnMissingBean(RedissonStringClient.class)
    public RedissonStringClient redissonStringClient() {
        return new RedissonStringClient();
    }

    @Bean
    @ConditionalOnMissingBean(RedissonManagerClient.class)
    public RedissonManagerClient redissonManagerClient() {
        return new RedissonManagerClient();
    }*/

}