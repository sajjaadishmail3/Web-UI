package saj.techie.catalgueui.controller;

import lombok.extern.slf4j.Slf4j;
import saj.techie.catalgueui.model.Book;
import saj.techie.catalgueui.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping({"/", "/books"})
@Slf4j
public class BookController {

    private final BookService bookService;
    public static final String FRAGMENTS_ERROR = "fragments/error";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String SUCCESS_MESSAGE = "successMessage";

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("bookTypes")
    public List<String> getBookTypes() {
        return Arrays.asList("HARD_COVER", "SOFT_COVER", "EBOOK", "AUDIOBOOK");
    }

    @GetMapping
    public String list(Model model) {
        try {
            model.addAttribute("books", bookService.findAll());
            return "books/list";
        } catch (Exception e) {
            log.error("Failed to get books", e);
            model.addAttribute(ERROR_MESSAGE, "Error: -> " + e.getMessage());
            return FRAGMENTS_ERROR;
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        try {
            model.addAttribute("book", new Book());
            model.addAttribute("formTitle", "Add New Book");
            return "books/form";
        } catch (Exception e) {
            log.error("Failed to create book form", e);
            model.addAttribute(ERROR_MESSAGE, e.getMessage());
            return FRAGMENTS_ERROR;
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            Book book = bookService.findById(id);
            if (book == null) {
                return "redirect:/books";
            }
            model.addAttribute("book", book);
            model.addAttribute("formTitle", "Edit Book");
            return "books/form";
        } catch (Exception e) {
            log.error("Failed to get book for edit", e);
            model.addAttribute(ERROR_MESSAGE, e.getMessage());
            return FRAGMENTS_ERROR;
        }
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("book") Book book,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", (book.getId() == null ? "Add New Book" : "Edit Book"));
            return "books/form";
        }
        try {
            log.info("Saving book: {}", book);
            bookService.save(book);
            log.info("Saved book: {}", book);
            redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Book saved successfully!");
            return "redirect:/books";
        } catch (Exception e) {
            log.error("Failed to save book", e);
            model.addAttribute(ERROR_MESSAGE, "Failed to save book. " + e.getMessage());
            model.addAttribute("formTitle", (book.getId() == null ? "Add New Book" : "Edit Book"));
            return "books/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            bookService.delete(id);
            redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Book deleted successfully!");
            return "redirect:/books";
        } catch (Exception e) {
            log.error("Failed to delete book", e);
            model.addAttribute(ERROR_MESSAGE, e.getMessage());
            return FRAGMENTS_ERROR;
        }
    }
}
