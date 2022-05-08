package xyz.a5s7.candles.service;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Candle {
    private final ZonedDateTime time;
    private final Double open;
    private final Double high;
    private final Double low;
    private final Double close;
    private final String symbol;

    public Candle(ZonedDateTime time, Double open, Double high, Double low, Double close, String symbol) {
        this.time = time;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.symbol = symbol;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public Double getOpen() {
        return open;
    }

    public Double getHigh() {
        return high;
    }

    public Double getLow() {
        return low;
    }

    public Double getClose() {
        return close;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candle candle = (Candle) o;
        return time.equals(candle.time) &&
                open.equals(candle.open) &&
                high.equals(candle.high) &&
                low.equals(candle.low) &&
                close.equals(candle.close) &&
                symbol.equals(candle.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, open, high, low, close, symbol);
    }

    @Override
    public String toString() {
        return "Candle{" +
                "time=" + time +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
