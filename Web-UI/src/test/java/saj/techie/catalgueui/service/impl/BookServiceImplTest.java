package saj.techie.catalgueui.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import saj.techie.catalgueui.client.BookManagementClient;
import saj.techie.catalgueui.model.Book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    private BookManagementClient client;
    private BookServiceImpl service;

    private Book sampleNewBook;
    private Book sampleExistingBook;

    @BeforeEach
    void setUp() {
        client = Mockito.mock(BookManagementClient.class);
        service = new BookServiceImpl(client);

        sampleNewBook = createBook(null);
        sampleExistingBook = createBook(1L);
    }

    @Test
    void findAll_delegatesToClient() {
        when(client.listBooks()).thenReturn(Arrays.asList(sampleExistingBook, sampleExistingBook));

        List<Book> books = service.findAll();

        assertEquals(2, books.size());
        verify(client, times(1)).listBooks();
    }

    @Test
    void findById_delegatesToClient() {
        when(client.getBook(5L)).thenReturn(sampleExistingBook);

        Book book = service.findById(5L);

        assertNotNull(book);
        assertEquals(sampleExistingBook.getTitle(), book.getTitle());
        verify(client).getBook(5L);
    }

    @Test
    void save_createsWhenIdIsNull() {
        when(client.createBook(any(Book.class))).thenReturn(sampleExistingBook);

        Book result = service.save(sampleNewBook);

        assertNotNull(result);
        verify(client, times(1)).createBook(sampleNewBook);
        verify(client, never()).updateBook(anyLong(), any(Book.class));
    }

    @Test
    void save_updatesWhenIdIsPresent() {
        when(client.updateBook(eq(1L), any(Book.class))).thenReturn(sampleExistingBook);

        Book result = service.save(sampleExistingBook);

        assertNotNull(result);
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(client, times(1)).updateBook(eq(1L), captor.capture());
        verify(client, never()).createBook(any(Book.class));
        assertEquals(1L, captor.getValue().getId());
    }

    @Test
    void delete_delegatesToClient() {
        service.delete(9L);
        verify(client, times(1)).deleteBook(9L);
    }

    private static Book createBook(Long id) {
        Book b = new Book();
        b.setId(id);
        b.setTitle("Test Title");
        b.setAuthor("Test Author");
        b.setIsbn("ISBN-123");
        b.setPublishedDate(LocalDate.of(2020, 1, 1));
        b.setPrice(new BigDecimal("100.00"));
        b.setType("HARD_COVER");
        return b;
    }
}
