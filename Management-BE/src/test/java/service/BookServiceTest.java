package service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import saj.techie.managementbe.model.Book;
import saj.techie.managementbe.service.BookService;
import saj.techie.managementbe.service.impl.BookServiceImpl;
import saj.techie.managementbe.repository.BookRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
    void test() {
        when(repository.findAll()).thenReturn(Lists.newArrayList(book, book));
        List<Book> books = service.findAll();
        assert books.size() == 2;
        assertEquals("Test Book", books.get(0).getTitle());
    }
}
