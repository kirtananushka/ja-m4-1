package com.tananushka.module04.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CachedValue {
    int attempts;
    LocalDateTime blockedTimestamp;
    LocalDateTime blockedUntilTimestamp;

    public void registerAttempt() {
        this.attempts++;
        log.info("Attempts: " + this.attempts);
    }
}
