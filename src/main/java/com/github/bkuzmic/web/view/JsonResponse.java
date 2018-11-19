package com.github.bkuzmic.web.view;

import java.util.List;

public class JsonResponse<T> {

    private final JsonExchange exchange;
    private final JsonPrinter<T> printer;

    public JsonResponse(JsonPrinter<T> printer, JsonExchange exchange) {
        this.printer = printer;
        this.exchange = exchange;
    }

    public void toJson(T body) {
        this.exchange.send(printer.toJson(body));
    }

    public void toJsonList(List<T> body) {
        this.exchange.send(printer.toJson(body));
    }
}
