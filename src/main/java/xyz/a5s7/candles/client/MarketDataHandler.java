package xyz.a5s7.candles.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xyz.a5s7.candles.service.CandleService;

@Component
public class MarketDataHandler implements WebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ObjectMapper mapper;
    private final CandleService candleService;

    public MarketDataHandler(ObjectMapper mapper, CandleService candleService) {
        this.mapper = mapper;
        this.candleService = candleService;
    }

    private Flux<? extends Tick.Data> receiveAll(WebSocketSession session) {
        return
            session
                .receive()
                    .map(WebSocketMessage::getPayloadAsText)
                    .concatMap(this::deserialize)
                    .flatMapIterable(Tick::getData)
                    //same symbol should be produced sequentially within consumer
//                    .groupBy(Tick.Data::getS)
//                    .map(g -> g.publishOn(Schedulers.newParallel("groupByPool", 16)))
                    .doOnNext(this::processMessage);
    }


    private Flux<Tick> deserialize(String s) {
        try {
            return Flux.just(mapper.readValue(s, Tick.class));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            return Flux.empty();
        }
    }

    private void processMessage(Tick.Data data) {
        candleService.handle(data);
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return receiveAll(session).then();
    }
}