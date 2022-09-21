package xyz.a5s7.candles.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.a5s7.candles.application.client.Tick;
import xyz.a5s7.candles.domain.Candle;
import xyz.a5s7.candles.domain.TimeFrames;
import xyz.a5s7.candles.domain.repo.CandleRepo;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
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
        Candle curCandle = map.putIfAbsent(ticker, newCandle);
        if (curCandle != null) {
            if (timeFrame.isSameTimeFrame(curCandle.getTime(), date)) {
                curCandle.updatePrice(data.getP());
                logger.debug("Updated candle {}", curCandle);
            } else {
                map.put(ticker, newCandle);
                candleRepo.addCandle(ticker, curCandle);
                logger.debug("Added candle to repo{}", curCandle);
            }
        } else {
            logger.debug("Added candle {}", newCandle);
        }
    }

    @Override
    public Collection<Candle> getCandles(String ticker) {
        Collection<Candle> candles = candleRepo.getCandles(ticker);
        Collection<Candle> out = new ArrayList<>();
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
