package com.tananushka.module04.model;

import java.time.LocalDateTime;

public class BlockedUser {
    private final String username;
    private final CachedValue cachedValue;

    public BlockedUser(String username, CachedValue cachedValue) {
        this.username = username;
        this.cachedValue = cachedValue;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getBlockedTimestamp() {
        return cachedValue.getBlockedTimestamp();
    }

    public LocalDateTime getBlockedUntilTimestamp() {
        return cachedValue.getBlockedUntilTimestamp();
    }
}
