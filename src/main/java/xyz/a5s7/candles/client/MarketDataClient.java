package xyz.a5s7.candles.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.Disposable;

import java.net.URI;

public class MarketDataClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Disposable subscription;
    private final WebSocketHandler webSocketSessionHandler;


    public MarketDataClient(WebSocketHandler webSocketSessionHandler) {
        this.webSocketSessionHandler = webSocketSessionHandler;
    }

    public void start(WebSocketClient webSocketClient, URI uri) {
        subscription =
            webSocketClient
                .execute(uri, webSocketSessionHandler)
                .subscribe();

        logger.info("Client started.");
    }

    public void stop() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }

        logger.info("Client stopped.");
    }
}