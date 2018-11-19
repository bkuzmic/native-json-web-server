package com.github.bkuzmic.web.view;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JsonResponseTest {

    private JsonResponse<String> jsonResponse;
    private JsonExchange mockExchange;

    @Before
    public void setUp() {
        mockExchange = mock(JsonExchange.class);

        this.jsonResponse = new JsonResponse<>(new JsonPrinter<String>() {
            @Override
            public String toJson(String object) {
                return "{\"test\":" + object + "}";
            }
        }, mockExchange);
    }

    @Test
    public void testToJson() {
        this.jsonResponse.toJson("1");
        verify(mockExchange).send(eq("{\"test\":1}"));
    }

    @Test
    public void testToJsonList() {
        List<String> testList = new ArrayList<>();
        testList.add("1");
        testList.add("2");
        testList.add("3");

        this.jsonResponse.toJsonList(testList);

        verify(mockExchange).send(eq("[{\"test\":1},{\"test\":2},{\"test\":3}]"));
    }

}
