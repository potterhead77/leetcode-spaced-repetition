package com.nandan.spaced_repetition.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.tinylog.Logger;

import java.util.concurrent.TimeUnit;

@EnableScheduling
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // dynamic cache creation (no specific names pre-defined)
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS) // Standard 1 hour cache time
                .recordStats();
    }

    @Scheduled(fixedRate = 7200000) // every 2 hours
    public void logCacheStats() {
        CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) cacheManager();
        for(String cacheName : caffeineCacheManager.getCacheNames()) {
            com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                    (com.github.benmanes.caffeine.cache.Cache<Object, Object>)
                            caffeineCacheManager.getCache(cacheName).getNativeCache();
            Logger.info("{} stats: {}", cacheName, nativeCache.stats());
        }
    }
}