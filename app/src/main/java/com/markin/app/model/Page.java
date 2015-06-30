package com.markin.app.model;

import java.util.Arrays;

/**
 * Created by wonmook on 15. 5. 13..
 */
public class Page {
    private Bookmark[] content;
    private boolean last;
    private int totalElements;
    private boolean firstPage;
    private boolean lastPage;
    private int totalPages;
    private int size;
    private int number;
    private Sort[] sort;
    private int numberOfElements;
    private boolean first;

    @Override
    public String toString() {
        return "Page{" +
                "content=" + Arrays.toString(content) +
                ", last=" + last +
                ", totalElements=" + totalElements +
                ", firstPage=" + firstPage +
                ", lastPage=" + lastPage +
                ", totalPages=" + totalPages +
                ", size=" + size +
                ", number=" + number +
                ", sort=" + Arrays.toString(sort) +
                ", numberOfElements=" + numberOfElements +
                ", first=" + first +
                '}';
    }

    public Bookmark[] getContent() {
        return content;
    }

    public void setContent(Bookmark[] content) {
        this.content = content;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Sort[] getSort() {
        return sort;
    }

    public void setSort(Sort[] sort) {
        this.sort = sort;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
}
