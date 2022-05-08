package xyz.a5s7.candles.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public enum TimeFrames implements TimeFrame {
    MINUTE {
        @Override
        public boolean isSameTimeFrame(ZonedDateTime c1, ZonedDateTime c2) {
            return c1.getMinute() == c2.getMinute() &&
                    Duration.between(c1, c2).getSeconds() < TimeUnit.MINUTES.toSeconds(1);
        }
    }
}
