package com.github.bkuzmic.web.view;

import java.util.List;
import java.util.StringJoiner;

public abstract class JsonPrinter<T> {

    public String toJson(List<T> list) {
        StringJoiner sj = new StringJoiner(",", "[", "]");
        for (T object : list) {
            sj.add(toJson(object));
        }
        return sj.toString();
    }

    public abstract String toJson(T object);


}
