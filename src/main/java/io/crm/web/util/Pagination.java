package io.crm.web.util;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by someone on 01/10/2015.
 */
final public class Pagination {
    public static final int defaultSize = 20;
    private final int page;
    private final int size;
    private final long total;
    private final int pageCount;

    public Pagination(int page, int size, long total) {
        this.size = size < 1 ? defaultSize : size;
        this.total = total < 0 ? 0 : total;
        pageCount = (int) ((total % size == 0) ? (total / size) : (total / size) + 1);
        this.page = page < 1 ? 1
                : page > pageCount ? pageCount
                : page;
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
        return pageFrom > 0 ? pageFrom - 1 : 1;
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
        final int trk = page - mid;
        System.out.println("nvLen = " + nvLen);
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