package saj.techie.managementbe.service;

import saj.techie.managementbe.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    Book create(Book book);
    Book update(Long id, Book book);
    void delete(Long id);
}
