package saj.techie.managementbe.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import saj.techie.managementbe.model.Book;
import saj.techie.managementbe.repository.BookRepository;
import saj.techie.managementbe.service.BookService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Book create(Book book) {
        book.setId(null);
        return repository.save(book);
    }

    @Override
    public Book update(Long id, Book book) {
        Book existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setIsbn(book.getIsbn());
        existing.setPublishedDate(book.getPublishedDate());
        existing.setPrice(book.getPrice());
        existing.setType(book.getType());
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Book not found: " + id);
        }
        repository.deleteById(id);
    }
}
