package xyz.a5s7.candles.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import xyz.a5s7.candles.application.client.MarketDataClient;
import xyz.a5s7.candles.application.client.MarketDataHandler;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class OnStartUp implements ApplicationListener<ApplicationReadyEvent> {
    @Value("${market.data.service.uri}")
    private String serviceUri;

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private MarketDataHandler handler;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        MarketDataClient marketDataClient = new MarketDataClient(handler);
        marketDataClient.start(webSocketClient, getURI());
    }

    private URI getURI() {
        try {
            return new URI(serviceUri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}