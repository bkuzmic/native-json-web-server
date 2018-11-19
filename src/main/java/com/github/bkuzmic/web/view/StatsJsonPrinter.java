package com.github.bkuzmic.web.view;

public class StatsJsonPrinter extends JsonPrinter<Integer> {

    @Override
    public String toJson(Integer object) {
        return String.format("{\"connection_pool_size\":%d}", object);
    }
}
