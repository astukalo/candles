package xyz.a5s7.candles.service;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MinuteTimeFrameTest {
    TimeFrames minuteFrame = TimeFrames.MINUTE;

    @Test
    void shouldBeInSameMinute() {
        assertTrue(
            minuteFrame.isSameTimeFrame(
                ZonedDateTime.parse("2022-04-23T04:30:05.123Z"),
                ZonedDateTime.parse("2022-04-23T04:30:45.123Z")
            )
        );
    }

    @Test
    void shouldBeInSameMinuteWhenStartAndEndOfMinute() {
        assertTrue(
            minuteFrame.isSameTimeFrame(
                    ZonedDateTime.parse("2022-04-23T04:30:00.123Z"),
                    ZonedDateTime.parse("2022-04-23T04:30:59.123Z")
            )
        );
    }

    @Test
    void shouldNotBeInSameMinuteWhenHourDifferent() {
        assertFalse(
            minuteFrame.isSameTimeFrame(
                    ZonedDateTime.parse("2022-04-23T04:30:05.123Z"),
                    ZonedDateTime.parse("2022-04-23T05:30:45.123Z")
            )
        );
    }

    @Test
    void shouldNotBeInSameMinute() {
        assertFalse(
            minuteFrame.isSameTimeFrame(
                    ZonedDateTime.parse("2022-04-23T04:30:05.123Z"),
                    ZonedDateTime.parse("2022-04-23T04:31:05.123Z")
            )
        );
    }
}