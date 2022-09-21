package xyz.a5s7.candles.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import xyz.a5s7.candles.domain.repo.CandleRepo;
import xyz.a5s7.candles.domain.service.CandleService;
import xyz.a5s7.candles.domain.service.MinuteCandleService;
import xyz.a5s7.candles.domain.TimeFrames;

@Configuration
public class AppConfig {
    @Bean
    WebSocketClient webSocketClient() {
        return new ReactorNettyWebSocketClient();
    }

    @Bean
    CandleService candleService(CandleRepo candleRepo) {
        return new MinuteCandleService(candleRepo, TimeFrames.MINUTE);
    }
}
