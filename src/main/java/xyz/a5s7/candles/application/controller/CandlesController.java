package xyz.a5s7.candles.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import xyz.a5s7.candles.domain.Candle;
import xyz.a5s7.candles.domain.service.CandleService;

@RestController
public class CandlesController {
    private final CandleService candleService;

    public CandlesController(CandleService candleService) {
        this.candleService = candleService;
    }

    @GetMapping("candles")
    public Flux<Candle> getCandles(@RequestParam String ticker) {
        return Flux.fromIterable(candleService.getCandles(ticker));
    }
}
