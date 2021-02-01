package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource(locations = {"classpath:application-test.properties"})
@ActiveProfiles("test")
@DataJpaTest
class BookTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book oneTestBook;

    @BeforeEach
    void setUp() {
        oneTestBook = new Book();
        oneTestBook.setGenre("Fantasy");
        oneTestBook.setAuthor("J. K. Rowling");
        oneTestBook.setImage("https://i.ebayimg.com/images/g/4qMAAOxygPtS1z9P/s-l500.jpg");
        oneTestBook.setTitle("Harry Potter");
        oneTestBook.setSubTitle("And The Philosopher's Stone");
        oneTestBook.setPublisher("Bloomsbury");
        oneTestBook.setYear("1997");
        oneTestBook.setPages(223);
        oneTestBook.setIsbn("0-7475-3269-9");

        entityManager.persist(oneTestBook);
        entityManager.flush();
    }

    @Test
    public void whenCreateBook_thenBookIsPersisted() {
        Book bookPersisted = bookRepository.findByIsbn("0-7475-3269-9").orElse(new Book());

        assertThat(bookPersisted.getAuthor().equals(oneTestBook.getAuthor())).isTrue();
        assertThat(bookPersisted.getPublisher().equals(oneTestBook.getPublisher())).isTrue();
        assertThat(bookPersisted.getGenre().equals(oneTestBook.getGenre())).isTrue();
    }

    @Test
    public void whenCreateBookWithoutAuthor_thenThrowException() {
        NullPointerException thrown = assertThrows(
                NullPointerException.class, () -> {
                    oneTestBook.setAuthor(null);
                    bookRepository.save(oneTestBook);
                });

        assertEquals("Please check the author supplied, its null!.", thrown.getMessage());
    }

    @Test
    public void whenFindAllBooks_thenReturnBooks() {
        Page<Book> booksFound = bookRepository
                .findAllBooks("Bloomsbury", "", "", "", "", "", "", 0, "", PageRequest
                        .of(0, 5, Sort.by("id")));
        assertNotNull(booksFound);
        assertFalse(booksFound.getContent().isEmpty());
    }

    @Test
    public void whenGetAllBooksThatNotExist_thenReturnError() {
        Page<Book> booksFound = bookRepository
                .findAllBooks("monkeys", "", "", "", "", "", "", 0, "", PageRequest
                        .of(0, 5, Sort.by("id")));
        assertTrue(booksFound.isEmpty());
    }
}
