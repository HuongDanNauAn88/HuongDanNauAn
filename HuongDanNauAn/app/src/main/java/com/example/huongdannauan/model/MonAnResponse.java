package com.example.huongdannauan.model;

import java.util.List;

public class MonAnResponse {
    private List<MonAn> results;
    private int offset;
    private int number;
    private int totalResults;

    // Getter và Setter
    public List<MonAn> getResults() {
        return results;
    }

    public void setResults(List<MonAn> results) {
        this.results = results;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
