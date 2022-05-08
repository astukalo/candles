package xyz.a5s7.candles.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import xyz.a5s7.candles.service.Candle;

import java.util.*;

/**
 * Not thread safe
 */
@Repository
public class CandleRepoInMemory implements CandleRepo {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, Deque<Candle>> map = new HashMap<>();
    private int maxSize = 100;

    @Override
    public void addCandle(String ticker, Candle candle) {
        Deque<Candle> candles = map.computeIfAbsent(ticker, k -> new LinkedList<>());
        candles.addLast(candle);
        if (candles.size() > maxSize) {
            candles.removeFirst();
            logger.debug("Removed first candle for ticker {}", ticker);
        }
    }

    @Override
    public Collection<Candle> getCandles(String ticker) {
        return map.get(ticker);
    }
}
