package io.crm.web.util;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by someone on 01/10/2015.
 */
final public class Pagination {
    public static final int DEFAULT_SIZE = 20;
    private final int page;
    private final int size;
    private final long total;
    private final int pageCount;

    public Pagination(int page, int size, long total) {
        this.size = size < 1 ? DEFAULT_SIZE : size;
        this.total = total < 0 ? 0 : total;
        pageCount = (int) ((total % size == 0) ? (total / size) : (total / size) + 1);
        this.page = page < 1 ? 1
                : page > pageCount ? pageCount
                : page;
    }

    public int getPage() {
        return page;
    }

    public int offset() {
        return (page - 1) * size;
    }

    public int getSize() {
        return size;
    }

    public long getTotal() {
        return total;
    }

    public int getPageCount() {
        return pageCount;
    }

    public boolean hasPrev() {
        return hasPrev(page);
    }

    public boolean hasPrev(int p) {
        return p > 1;
    }

    public boolean hasNext() {
        return hasNext(page);
    }

    public boolean hasNext(int p) {
        return p < pageCount;
    }

    public boolean isFirst() {
        return isFirst(page);
    }

    public boolean isFirst(int p) {
        return p <= first();
    }

    public boolean isLast() {
        return isLast(page);
    }

    public boolean isLast(int p) {
        return p >= last();
    }

    public boolean isCurrentPage(final int aPage) {
        return page == aPage;
    }

    public int next() {
        return next(page);
    }

    public int prev() {
        return prev(page);
    }

    public int next(int pageTo) {
        return pageTo < pageCount ? pageTo + 1 : pageCount;
    }

    public int prev(int pageFrom) {
        return pageFrom > 1 ? pageFrom - 1 : 1;
    }

    public int first() {
        return 1;
    }

    public int last() {
        return pageCount;
    }

    public List<Integer> nav(int navLength) {
        final ImmutableList.Builder<Integer> builder = ImmutableList.builder();
        navLength = navLength < 2 ? 2 : navLength;
        final int nvLen = navLength > pageCount ? pageCount : navLength;
        if (nvLen == 1) {
            return builder.add(page).build();
        }
        final int mid = nvLen / 2;
        int trk = page - mid;
        trk = trk < 1 ? 1 : trk;

        for (int i = 0; i < nvLen; i++) {
            final int track = trk + i;
            if (track >= 1 && track <= pageCount) builder.add(track);
        }
        return builder.build();
    }

    public static void main(String... args) {
        System.out.println(String.join(", ", Arrays.asList(
                new Pagination(8, 20, 200).nav(5))
                .stream().map(t -> {
                    return t + "";
                }).collect(Collectors.toList())));
    }
}
