package com.github.bkuzmic.web.view;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class JsonHttpServerExchange implements JsonExchange {

    private static final String CONTENT_TYPE_HEADER_VALUE ="application/json";

    private final HttpServerExchange exchange;

    public JsonHttpServerExchange(HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void send(String body) {
        this.exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, CONTENT_TYPE_HEADER_VALUE);
        this.exchange.getResponseSender().send(body);
    }

}
