package com.example.androidtask.response;


import java.util.List;

public class Data<T> {

    private List<T> records;
    private int total;
    private int size;
    private int current;
    public void setRecords(List<T> records) {
        this.records = records;
    }
    public List<T> getRecords() {
        return records;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
    public int getCurrent() {
        return current;
    }

    @Override
    public String toString() {
        return "Data{" +
                "records=" + records +
                ", total=" + total +
                ", size=" + size +
                ", current=" + current +
                '}';
    }
}