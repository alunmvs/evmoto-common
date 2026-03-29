package com.evmoto.common.redis.client;

import com.evmoto.common.redis.able.LockInterface;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedissonLockClient {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取读写锁
     */
    public <T> T lock(DoReturnSomething<T> doSomething, LockInterface lockInterface, String... keys) {
        RLock lock = this.getLock(lockInterface, keys);
        lock.lock();
        try {
            return doSomething.apply();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取读写锁
     */
    public void lock(DoSomething doSomething, LockInterface lockInterface, String... keys) {
        RLock lock = this.getLock(lockInterface, keys);
        lock.lock();
        try {
            doSomething.accept();
        } finally {
            lock.unlock();
        }
    }

    public RLock getLock(LockInterface lockEnum, String... params) {
        String key = lockEnum.formatKeyWithParam(params);
        return redissonClient.getLock(key);
    }



    @FunctionalInterface
    public interface DoReturnSomething<T> {
        T apply();
    }

    @FunctionalInterface
    public interface DoSomething {
        void accept();
    }

}
