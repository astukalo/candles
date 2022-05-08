package xyz.a5s7.candles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.a5s7.candles.client.Tick;
import xyz.a5s7.candles.repo.CandleRepo;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;

public class MinuteCandleService implements CandleService {
    static final ZoneId DEFAULT_TIMEZONE = ZoneId.of("UTC");

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CandleRepo candleRepo;
    private final TimeFrames timeFrame;
    final ConcurrentHashMap<String, Candle> map = new ConcurrentHashMap<>();

    public MinuteCandleService(CandleRepo candleRepo, TimeFrames timeFrame) {
        this.candleRepo = candleRepo;
        this.timeFrame = timeFrame;
    }

    @Override
    public void handle(Tick.Data data) {
        ZonedDateTime date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(data.getT()), DEFAULT_TIMEZONE);
        String ticker = data.getS();

        Candle newCandle = new Candle(date, data.getP(), data.getP(), data.getP(), data.getP(), ticker);
        Candle lastCandle = map.put(ticker, newCandle);
        if (lastCandle != null) {
            synchronized (lastCandle) {
                if (timeFrame.isSameTimeFrame(lastCandle.getTime(), date)) {
                    newCandle = new Candle(lastCandle.getTime(),
                            lastCandle.getOpen(),
                            Math.max(lastCandle.getHigh(), data.getP()),
                            Math.min(lastCandle.getLow(), data.getP()),
                            data.getP(),
                            ticker);
                    logger.debug("Updating candle {} with {}", lastCandle, newCandle);
                    map.put(ticker, newCandle);
                } else {
                    logger.debug("Adding candle {}", newCandle);
                    candleRepo.addCandle(ticker, lastCandle);
                }
            }
        } else {
            logger.debug("Adding candle {}", newCandle);
        }
    }

    @Override
    public Collection<Candle> getCandles(String ticker) {
        Collection<Candle> candles = candleRepo.getCandles(ticker);
        Collection<Candle> out = new LinkedHashSet<>();
        if (candles != null) {
            out.addAll(candles);
        }
        Candle e = map.get(ticker);
        if (e != null) {
            out.add(e);
        }
        return out;
    }

}
