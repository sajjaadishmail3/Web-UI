package saj.techie.managementbe.service.impl;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import saj.techie.managementbe.model.Book;
import saj.techie.managementbe.service.BookService;
import saj.techie.managementbe.repository.BookRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    private BookService service;
    private final BookRepository repository = Mockito.mock(BookRepository.class);
    private final Book book = new Book();

    @BeforeEach
    void setUp() {
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.valueOf(100.00));
        book.setPublishedDate(LocalDate.now());
        service = new BookServiceImpl(repository);
    }

    @Test
    void testFindAll_Success() {
        when(repository.findAll()).thenReturn(Lists.newArrayList(book, book));

        List<Book> books = service.findAll();

        assertEquals(2, books.size());
        assertEquals("Test Book", books.get(1).getTitle());
    }

    @Test
    void testFindById_Success() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));

        Book b = service.findById(anyLong()).get();

        assertEquals("Test Book", b.getTitle());
    }

    @Test
    void testCreate_Success() {
        when(repository.save(any(Book.class))).thenReturn(book);

        Book b = service.create(book);

        assertEquals("1234567890", b.getIsbn());
    }

    @Test
    void testDelete_Success() {
        when(repository.existsById(anyLong())).thenReturn(true);

        service.delete(anyLong());

        verify(repository,times(1)).deleteById(anyLong());
    }

    @Test
    void testDelete_BookNotFoundException() {
        assertThrows(NoSuchElementException.class,  () -> service.delete(anyLong()));
    }

    @Test
    void testUpdate_Success() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));

        Book b = service.update(1L, book);

        verify(repository,times(1)).save(any(Book.class));
    }
}
