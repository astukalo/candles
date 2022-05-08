package xyz.a5s7.candles.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.a5s7.candles.client.Tick;
import xyz.a5s7.candles.repo.CandleRepo;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static xyz.a5s7.candles.service.MinuteCandleService.DEFAULT_TIMEZONE;


@ExtendWith(MockitoExtension.class)
class MinuteCandleServiceTest {
    @Mock
    CandleRepo candleRepo;

    @Mock
    TimeFrames timeFrames;

    @InjectMocks
    private MinuteCandleService minuteCandleService;

    private static final String symbol = "T";

    @Test
    void shouldAddNewCandleForTickerIfTickerNotFound() {
        long time = System.currentTimeMillis();
        double price = 100.34539;

        minuteCandleService.handle(new Tick.Data(price, symbol, time, 10));

        Candle expectedCandle = candle(symbol, price, timeFromLong(time));
        Collection<Candle> candles = minuteCandleService.getCandles(symbol);
        assertEquals(1, candles.size());
        assertEquals(expectedCandle, candles.stream().findFirst().get());

        verify(candleRepo, never()).addCandle(eq(symbol), eq(expectedCandle));
    }

    @Test
    void shouldAddNewCandleForTickerIfNotFoundForTimeFrame() {
        long time = System.currentTimeMillis();
        double price = 100.34539;
        Candle lastCandle = candle(symbol, price, timeFromLong(time));
        long newTickTime = time + TimeUnit.SECONDS.toMillis(61);
        ZonedDateTime newCandleStart = timeFromLong(newTickTime);
        minuteCandleService.handle(new Tick.Data(price, symbol, time, 10));

        when(timeFrames.isSameTimeFrame(lastCandle.getTime(), newCandleStart)).thenReturn(false);

        minuteCandleService.handle(new Tick.Data(price, symbol, newTickTime, 10));

        verify(candleRepo).addCandle(eq(symbol), eq(lastCandle));
    }

    @Test
    void shouldUpdateCandleForTickerIfInTimeFrameAndNewPriceIsHigher() {
        long time = System.currentTimeMillis();
        double price = 100.34539;
        ZonedDateTime candleStart = timeFromLong(time);
        Candle lastCandle = candle(symbol, price, candleStart);
        long newTickTime = time + TimeUnit.SECONDS.toMillis(45);
        double newPrice = 109.34539;
        ZonedDateTime newCandleStart = timeFromLong(newTickTime);

        minuteCandleService.handle(new Tick.Data(price, symbol, time, 10));
        when(timeFrames.isSameTimeFrame(lastCandle.getTime(), newCandleStart)).thenReturn(true);

        minuteCandleService.handle(new Tick.Data(newPrice, symbol, newTickTime, 10));

        verify(candleRepo, never()).addCandle(eq(symbol), any());

        Candle expectedCandle = new Candle(candleStart, price, newPrice, price, newPrice, symbol);
        Collection<Candle> candles = minuteCandleService.getCandles(symbol);
        assertEquals(1, candles.size());
        assertEquals(expectedCandle, candles.stream().findFirst().get());
    }

    @Test
    void shouldUpdateCandleForTickerIfInTimeFrameAndNewPriceIsLower() {
        long time = System.currentTimeMillis();
        double price = 100.34539;
        ZonedDateTime candleStart = timeFromLong(time);
        Candle lastCandle = candle(symbol, price, candleStart);
        long newTickTime = time + TimeUnit.SECONDS.toMillis(45);
        double newPrice = 99.34539;
        ZonedDateTime newCandleStart = timeFromLong(newTickTime);

        minuteCandleService.handle(new Tick.Data(price, symbol, time, 10));
        when(timeFrames.isSameTimeFrame(lastCandle.getTime(), newCandleStart)).thenReturn(true);

        minuteCandleService.handle(new Tick.Data(newPrice, symbol, newTickTime, 10));

        Candle expectedCandle = new Candle(candleStart, price, price, newPrice, newPrice, symbol);
        Collection<Candle> candles = minuteCandleService.getCandles(symbol);
        assertEquals(1, candles.size());
        assertEquals(expectedCandle, candles.stream().findFirst().get());
    }

    @Test
    void shouldReturnEmptyCollectionIfNoCandle() {
        Collection<Candle> candles = minuteCandleService.getCandles(symbol);
        assertTrue(candles.isEmpty());
    }

    @Test
    void shouldReturnListOfCandlesFromRepoAndLast() {
        long time = System.currentTimeMillis();
        double price = 100.34539;
        List<Candle> candleList = List.of(
                candle(symbol, 10, timeFromLong(time - 165)),
                candle(symbol, 12, timeFromLong(time - 65))
        );
        Collection<Candle> expectedCollection = new LinkedHashSet<>(candleList);
        expectedCollection.add(candle(symbol, price, timeFromLong(time)));

        when(candleRepo.getCandles(symbol)).thenReturn(
                candleList);

        minuteCandleService.handle(new Tick.Data(price, symbol, time, 10));

        Collection<Candle> candles = minuteCandleService.getCandles(symbol);
        assertEquals(3, candles.size());
        assertEquals(expectedCollection, candles);
    }

    @BeforeEach
    void init() {
        minuteCandleService.map.clear();
    }

    private Candle candle(String symbol, double price, ZonedDateTime zonedDateTime) {
        return new Candle(zonedDateTime, price, price, price, price, symbol);
    }

    private ZonedDateTime timeFromLong(long time) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), DEFAULT_TIMEZONE);
    }
}