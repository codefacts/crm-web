package io.crm.web.util;

import io.crm.web.util.Pagination;

import java.util.List;

/**
 * Created by someone on 28/10/2015.
 */
final public class RspList<T> {
    private List<T> data;
    private Pagination pagination;

    public RspList(List<T> data, Pagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<T> getData() {
        return data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public static class Props {
        public static final String data = "data";
        public static final String pagination = "pagination";
    }
}
