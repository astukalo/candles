package xyz.a5s7.candles.domain;

import java.time.ZonedDateTime;

public interface TimeFrame {
    boolean isSameTimeFrame(ZonedDateTime c1, ZonedDateTime c2);
}
