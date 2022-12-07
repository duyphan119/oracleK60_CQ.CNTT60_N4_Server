package com.api.shoesshop.types;

public class FindAll<T> {
    private T items;
    private long count;

    public FindAll(T items, long count) {
        this.items = items;
        this.count = count;
    }

    public T getItems() {
        return this.items;
    }

    public void setItems(T items) {
        this.items = items;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
