package co.com.asulado.model.pageable;

import java.util.List;

public class Pageable<T> {
    private List<T> elements;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public Pageable(List<T> elements, int page, int size, long totalElements) {
        this.elements = elements;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        this.last = this.page + 1 >= this.totalPages;
    }

    public static <T> Pageable<T> of(List<T> all, int page, int size) {
        int fromIndex = Math.min(page * size, all.size());
        int toIndex = Math.min(fromIndex + size, all.size());
        List<T> sublist = all.subList(fromIndex, toIndex);
        return new Pageable<>(sublist, page, size, all.size());
    }

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
