package xyz.a5s7.candles.repo;

import xyz.a5s7.candles.service.Candle;

import java.util.Collection;

public interface CandleRepo {
    void addCandle(String ticker, Candle candle);

    Collection<Candle> getCandles(String ticker);
}
