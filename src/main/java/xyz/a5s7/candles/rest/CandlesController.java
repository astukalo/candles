package xyz.a5s7.candles.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import xyz.a5s7.candles.service.Candle;
import xyz.a5s7.candles.service.CandleService;

@RestController
public class CandlesController {
    private CandleService candleService;

    public CandlesController(CandleService candleService) {
        this.candleService = candleService;
    }

    @GetMapping("candles")
    public Flux<Candle> getCandles(@RequestParam String ticker) {
        return Flux.fromIterable(candleService.getCandles(ticker));
    }
}
