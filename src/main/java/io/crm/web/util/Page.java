package io.crm.web.util;

import java.util.List;

/**
 * Created by someone on 26/10/2015.
 */
public class Page<T> {
    private List<T> content;
    private int number;

    public Page(List<T> resultList, int page, int size, Long total) {

    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return 0;
    }

    public int getSize() {
        return 0;
    }

    public int getTotalPages() {
        return 0;
    }

    public int getTotalElements() {
        return 0;
    }
}
