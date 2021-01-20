package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * This method consults a book by author
     *
     * @param author: this is the author of the book
     * @return {@link Book}
     */
    Optional<Book> findByAuthor(String author);

    /**
     * Method that allows to bring all the filtered books
     *
     * @param publisher: this is the publisher the book
     * @param genre:     this is the genre the book
     * @param year:      this is the year the book
     * @return {@link List<Book>}
     */
    @Query("SELECT bk FROM Book AS bk "
            + "WHERE (:publisher is null OR bk.publisher = :publisher) "
            + "AND (:genre is null OR bk.genre = :genre) "
            + "AND (:year is null OR bk.year = :year) ")
    List<Book> getAllBook(@Param("publisher") String publisher, @Param("genre") String genre,
            @Param("year") String year);
}
