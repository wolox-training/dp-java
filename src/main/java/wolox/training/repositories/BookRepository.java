package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wolox.training.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * This method consults a book by author
     *
     * @param author
     * @return returns a single book
     */
    Optional<Book> findByAuthor(String author);
}
