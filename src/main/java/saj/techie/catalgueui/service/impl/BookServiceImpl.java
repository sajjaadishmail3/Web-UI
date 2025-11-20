package saj.techie.catalgueui.service.impl;

import saj.techie.catalgueui.client.BookManagementClient;
import saj.techie.catalgueui.model.Book;
import org.springframework.stereotype.Service;
import saj.techie.catalgueui.service.BookService;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookManagementClient client;

    public BookServiceImpl(BookManagementClient client) {
        this.client = client;
    }

    @Override
    public List<Book> findAll() {
        return client.listBooks();
    }

    @Override
    public Book findById(Long id) {
        return client.getBook(id);
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            return client.createBook(book);
        } else {
            return client.updateBook(book.getId(), book);
        }
    }

    @Override
    public void delete(Long id) {
        client.deleteBook(id);
    }
}
