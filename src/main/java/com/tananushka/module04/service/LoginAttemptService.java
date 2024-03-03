package com.tananushka.module04.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.tananushka.module04.model.BlockedUser;
import com.tananushka.module04.model.CachedValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoginAttemptService {

    private final int maxAttempt;
    private final int blockDurationSec;
    private final LoadingCache<String, CachedValue> attemptsCache;

    public LoginAttemptService(@Value("${login.attempt.max}") int maxAttempt,
                               @Value("${login.attempt.block-duration-sec}") int blockDurationSec) {

        this.maxAttempt = maxAttempt;
        this.blockDurationSec = blockDurationSec;

        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(blockDurationSec, TimeUnit.SECONDS)
                .build(new CacheLoader<>() {
                    public CachedValue load(String key) {
                        return new CachedValue(0, null, null);
                    }
                });
    }

    public void loginFailed(final String key) {
        try {
            var cachedValue = attemptsCache.get(key);
            cachedValue.registerAttempt();

            if (isBlocked(key) && cachedValue.getBlockedTimestamp() == null) {
                cachedValue.setBlockedTimestamp(LocalDateTime.now());
                cachedValue.setBlockedUntilTimestamp(cachedValue.getBlockedTimestamp().plusSeconds(blockDurationSec));
            }

            attemptsCache.put(key, cachedValue);
        } catch (final ExecutionException e) {
            log.error("Failed to get cached value", e);
        }
    }

    public boolean isBlocked(final String key) {
        try {
            return attemptsCache.get(key).getAttempts() >= maxAttempt;
        } catch (final ExecutionException e) {
            return false;
        }
    }

    public void loginSucceeded(String key) {
        CachedValue cachedValue = new CachedValue(0, null, null);
        log.info("Set failed attempts to 0");
        attemptsCache.put(key, cachedValue);
    }

    public List<BlockedUser> retrieveBlockedUsers() {
        return attemptsCache.asMap().entrySet().stream()
                .filter(entry -> isBlocked(entry.getKey()))
                .map(entry -> new BlockedUser(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
