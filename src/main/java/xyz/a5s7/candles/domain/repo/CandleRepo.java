package xyz.a5s7.candles.domain.repo;

import xyz.a5s7.candles.domain.Candle;

import java.util.Collection;

public interface CandleRepo {
    void addCandle(String ticker, Candle candle);

    Collection<Candle> getCandles(String ticker);
}
