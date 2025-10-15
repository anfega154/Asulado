package co.com.asulado.model.pageable;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageableTest {

    @Test
    void shouldCreatePageableCorrectlyUsingConstructor() {
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");

        Pageable<String> pageable = new Pageable<>(elements, 0, 2, elements.size());

        assertEquals(elements, pageable.getElements());
        assertEquals(0, pageable.getPage());
        assertEquals(2, pageable.getSize());
        assertEquals(5, pageable.getTotalElements());
        assertEquals(3, pageable.getTotalPages());
        assertFalse(pageable.isLast()); // page 0 -> not last
    }

    @Test
    void shouldMarkLastPageCorrectly() {
        List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5);

        Pageable<Integer> pageable = new Pageable<>(elements, 2, 2, elements.size());

        assertTrue(pageable.isLast()); // page 2 of 3 -> last page
    }

    @Test
    void shouldHandleEmptyListGracefully() {
        Pageable<String> pageable = new Pageable<>(Collections.emptyList(), 0, 10, 0);

        assertTrue(pageable.getElements().isEmpty());
        assertEquals(0, pageable.getTotalPages());
        assertTrue(pageable.isLast());
    }

    @Test
    void shouldCreatePageableUsingOfMethod() {
        List<String> all = Arrays.asList("A", "B", "C", "D", "E");

        Pageable<String> pageable = Pageable.of(all, 1, 2); // page 1 -> elements [C, D]

        assertEquals(Arrays.asList("C", "D"), pageable.getElements());
        assertEquals(1, pageable.getPage());
        assertEquals(2, pageable.getSize());
        assertEquals(5, pageable.getTotalElements());
        assertEquals(3, pageable.getTotalPages());
        assertFalse(pageable.isLast());
    }

    @Test
    void shouldHandleLastPartialPageCorrectly() {
        List<String> all = Arrays.asList("A", "B", "C", "D", "E");

        Pageable<String> pageable = Pageable.of(all, 2, 2); // last page (index 2) -> element [E]

        assertEquals(Collections.singletonList("E"), pageable.getElements());
        assertTrue(pageable.isLast());
    }

    @Test
    void shouldHandlePageOutOfRangeGracefully() {
        List<String> all = Arrays.asList("A", "B", "C");

        Pageable<String> pageable = Pageable.of(all, 5, 2);

        assertTrue(pageable.getElements().isEmpty());
        assertTrue(pageable.isLast());
    }

    @Test
    void shouldAllowSettersToModifyValues() {
        Pageable<String> pageable = new Pageable<>(Collections.emptyList(), 0, 0, 0);

        pageable.setElements(Arrays.asList("X", "Y"));
        pageable.setPage(1);
        pageable.setSize(5);
        pageable.setTotalElements(10);
        pageable.setTotalPages(2);
        pageable.setLast(true);

        assertEquals(Arrays.asList("X", "Y"), pageable.getElements());
        assertEquals(1, pageable.getPage());
        assertEquals(5, pageable.getSize());
        assertEquals(10, pageable.getTotalElements());
        assertEquals(2, pageable.getTotalPages());
        assertTrue(pageable.isLast());
    }
}
