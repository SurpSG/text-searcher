package com.gnatiuk.searcher.ui.utils.filters.components.tools;

public class IndexedData<T>{

    private static int ID = 0;
    private final int id;

    private T data;

    public IndexedData(T data) {
        id = ID++;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "IndexedData{" +
                "id=" + id +
                ", data=" + data +
                '}';
    }
}