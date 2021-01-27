package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    List<Book> getAllBooksMatch(@Param("publisher") String publisher, @Param("genre") String genre,
            @Param("year") String year);


    /**
     * This method performs a search by ISBN
     *
     * @param isbn: this is the book identification
     * @return {@link Book}
     */
    Optional<Book> findByIsbn(String isbn);


    /**
     * Method that allows to bring all the filtered books
     *
     * @param publisher: book publisher
     * @param genre:     book genre
     * @param year:      year of publication of the book
     * @param author:    author of the book
     * @param image:     picture or book cover
     * @param title:     title of the book
     * @param subtitle:  book subtitle
     * @param pages:     pages contained in the book
     * @param isbn:      book identifier
     * @param pageable:  object that allows us to order and paginate the query
     * @return {@link Page<Book>}
     */
    @Query("SELECT bk FROM Book bk "
            + "WHERE (:publisherName = '' OR LOWER(bk.publisher) = LOWER(:publisherName)) "
            + "AND (:genre = '' OR LOWER(bk.genre) = LOWER(:genre)) "
            + "AND (:year = '' OR bk.year = :year)"
            + "AND (:author = '' OR LOWER(bk.author) = LOWER(:author))"
            + "AND (:image = '' OR bk.image = :image)"
            + "AND (:title = '' OR LOWER(bk.title) = LOWER(:title))"
            + "AND (:subtitle = '' OR LOWER(bk.subTitle) = LOWER(:subtitle))"
            + "AND (:pages = 0 OR bk.pages = :pages)"
            + "AND (:isbn = '' OR bk.isbn = :isbn)")
    Page<Book> findAllBooks(@Param("publisherName") String publisher,
            @Param("genre") String genre, @Param("year") String year,
            @Param("author") String author,
            @Param("image") String image, @Param("title") String title,
            @Param("subtitle") String subtitle,
            @Param("pages") Integer pages, @Param("isbn") String isbn,
            Pageable pageable);
}
