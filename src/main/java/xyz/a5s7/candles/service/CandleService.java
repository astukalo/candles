package xyz.a5s7.candles.service;

import xyz.a5s7.candles.client.Tick;

import java.util.Collection;

public interface CandleService {
    void handle(Tick.Data data);

    Collection<Candle> getCandles(String ticker);
}
