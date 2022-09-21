package xyz.a5s7.candles.domain.service;

import xyz.a5s7.candles.application.client.Tick;
import xyz.a5s7.candles.domain.Candle;

import java.util.Collection;

public interface CandleService {
    void handle(Tick.Data data);

    Collection<Candle> getCandles(String ticker);
}
