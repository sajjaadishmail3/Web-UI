package saj.techie.managementbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import saj.techie.managementbe.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
