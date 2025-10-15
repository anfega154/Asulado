package co.com.asulado.model.pageable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Pagination {
    private Pagination() {
    }

    public static <T, V extends Comparable<? super V>> Pageable<T> paginateAndSort(
            List<T> items,
            int page,
            int size,
            Function<T, V> sortKeyExtractor
    ) {
        Comparator<T> comparator = Comparator.comparing(sortKeyExtractor, Comparator.nullsLast(Comparator.naturalOrder()));

        items.sort(comparator);

        return Pageable.of(items, page, size);
    }
}
