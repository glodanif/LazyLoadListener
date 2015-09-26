package com.g.lazyloadlistenersample.data;

import java.util.List;

public class BooksResponse {

    private String kind;

    private int totalItems;

    private List<Book> items;

    public String getKind() {
        return kind;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public List<Book> getItems() {
        return items;
    }
}
