package xyz.a5s7.candles.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Candle {
    private final ZonedDateTime time;
    private final Double open;
    private Double high;
    private Double low;
    private Double close;
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
        return time.equals(candle.time) && open.equals(candle.open) && symbol.equals(candle.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, open, symbol);
    }

    public synchronized void updatePrice(Double price) {
        this.low = Math.min(low, price);
        this.high = Math.max(high, price);
        this.close = price;
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
