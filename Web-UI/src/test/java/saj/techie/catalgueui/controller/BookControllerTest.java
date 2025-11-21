package saj.techie.catalgueui.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import saj.techie.catalgueui.model.Book;
import saj.techie.catalgueui.service.BookService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    private BookService bookService;
    private BookController controller;

    @BeforeEach
    void setUp() {
        bookService = Mockito.mock(BookService.class);
        controller = new BookController(bookService);
    }

    @Test
    void getBookTypes_returnsAllTypes() {
        List<String> types = controller.getBookTypes();
        assertEquals(4, types.size());
        assertTrue(types.containsAll(Arrays.asList("HARD_COVER", "SOFT_COVER", "EBOOK", "AUDIOBOOK")));
    }

    @Test
    void list_success() {
        when(bookService.findAll()).thenReturn(Arrays.asList(sampleBook(1L)));
        Model model = new ConcurrentModel();

        String view = controller.list(model);

        assertEquals("books/list", view);
        assertTrue(model.containsAttribute("books"));
    }

    @Test
    void list_error() {
        when(bookService.findAll()).thenThrow(new RuntimeException("Boom"));
        Model model = new ConcurrentModel();

        String view = controller.list(model);

        assertEquals(BookController.FRAGMENTS_ERROR, view);
        assertTrue(model.containsAttribute("errorMessage"));
    }

    @Test
    void createForm_success() {
        Model model = new ConcurrentModel();
        String view = controller.createForm(model);
        assertEquals("books/form", view);
        assertTrue(model.containsAttribute("book"));
        assertEquals("Add New Book", model.getAttribute("formTitle"));
    }

    @Test
    void editForm_success() {
        Book book = sampleBook(7L);
        when(bookService.findById(7L)).thenReturn(book);
        Model model = new ConcurrentModel();

        String view = controller.editForm(7L, model);

        assertEquals("books/form", view);
        assertEquals(book, model.getAttribute("book"));
        assertEquals("Edit Book", model.getAttribute("formTitle"));
    }

    @Test
    void editForm_notFound_redirectsToList() {
        when(bookService.findById(99L)).thenReturn(null);
        Model model = new ConcurrentModel();

        String view = controller.editForm(99L, model);

        assertEquals("redirect:/books", view);
    }

    @Test
    void editForm_error() {
        when(bookService.findById(1L)).thenThrow(new RuntimeException("err"));
        Model model = new ConcurrentModel();

        String view = controller.editForm(1L, model);

        assertEquals(BookController.FRAGMENTS_ERROR, view);
        assertTrue(model.containsAttribute("errorMessage"));
    }

    @Test
    void save_withBindingErrors_newBook_returnsForm() {
        Book book = sampleBook(null); // new book
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        Model model = new ConcurrentModel();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String view = controller.save(book, bindingResult, model, redirectAttributes);

        assertEquals("books/form", view);
        assertEquals("Add New Book", model.getAttribute("formTitle"));
        verify(bookService, never()).save(any());
    }

    @Test
    void save_withBindingErrors_existingBook_returnsForm() {
        Book book = sampleBook(5L); // existing book
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        Model model = new ConcurrentModel();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String view = controller.save(book, bindingResult, model, redirectAttributes);

        assertEquals("books/form", view);
        assertEquals("Edit Book", model.getAttribute("formTitle"));
        verify(bookService, never()).save(any());
    }

    @Test
    void save_success_redirectsWithFlash() {
        Book book = sampleBook(null);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        Model model = new ConcurrentModel();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String view = controller.save(book, bindingResult, model, redirectAttributes);

        assertEquals("redirect:/books", view);
        verify(bookService, times(1)).save(book);
        verify(redirectAttributes, times(1))
                .addFlashAttribute(eq("successMessage"), anyString());
    }

    @Test
    void save_failure_returnsFormWithError() {
        Book book = sampleBook(null);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException("fail")).when(bookService).save(any(Book.class));
        Model model = new ConcurrentModel();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String view = controller.save(book, bindingResult, model, redirectAttributes);

        assertEquals("books/form", view);
        assertEquals("Add New Book", model.getAttribute("formTitle"));
        assertTrue(model.containsAttribute("errorMessage"));
    }

    @Test
    void delete_success_redirectsWithFlash() {
        Model model = new ConcurrentModel();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String view = controller.delete(3L, model, redirectAttributes);

        assertEquals("redirect:/books", view);
        verify(bookService, times(1)).delete(3L);
        verify(redirectAttributes).addFlashAttribute(eq("successMessage"), anyString());
    }

    @Test
    void delete_failure_returnsErrorFragment() {
        doThrow(new RuntimeException("oops")).when(bookService).delete(10L);
        Model model = new ConcurrentModel();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String view = controller.delete(10L, model, redirectAttributes);

        assertEquals(BookController.FRAGMENTS_ERROR, view);
        assertTrue(model.containsAttribute("errorMessage"));
    }

    private static Book sampleBook(Long id) {
        Book b = new Book();
        b.setId(id);
        b.setTitle("Title");
        b.setAuthor("Author");
        b.setIsbn("ISBN");
        b.setPublishedDate(LocalDate.of(2020, 1, 1));
        b.setPrice(new BigDecimal("10.00"));
        b.setType("HARD_COVER");
        return b;
    }
}
