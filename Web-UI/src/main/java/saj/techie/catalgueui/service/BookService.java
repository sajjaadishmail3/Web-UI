package saj.techie.catalgueui.service;

import saj.techie.catalgueui.model.Book;

public interface BookService {
    Iterable<Book> findAll();
    Book findById(Long id);
    Book save(Book book);
    void delete(Long id);
}
