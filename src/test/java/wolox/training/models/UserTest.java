package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User oneTestUser;

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

        oneTestUser = new User();
        oneTestUser.setUsername("SamusKitchen");
        oneTestUser.setName("Samurai Kitchen");
        oneTestUser.setBirthDate(LocalDate.of(1989, 10, 16));
        oneTestUser.setBooks(new LinkedList<>(Collections.singletonList(oneTestBook)));

        entityManager.persist(oneTestBook);
        entityManager.persist(oneTestUser);
        entityManager.flush();
    }

    @Test
    public void whenCreateUser_thenUserIsPersisted() {
        User userPersisted = userRepository.findByUsername("SamusKitchen").orElse(new User());

        assertThat(userPersisted.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(userPersisted.getName().equals(oneTestUser.getName())).isTrue();
        assertThat(userPersisted.getBirthDate().equals(oneTestUser.getBirthDate())).isTrue();
        assertThat(userPersisted.getBooks().size() == oneTestUser.getBooks().size()).isTrue();
    }

    @Test
    public void whenCreateUserWithoutUsername_thenThrowException() {
        NullPointerException thrown = assertThrows(
                NullPointerException.class, () -> {
                    oneTestUser.setUsername(null);
                    userRepository.save(oneTestUser);
                });

        assertEquals("Please check the username supplied, its null!.", thrown.getMessage());
    }

    @Test
    public void whenFindByUsername_thenReturnUser() {
        Optional<User> userFound = userRepository.findByUsername("SamusKitchen");
        assertThat(oneTestUser.equals(userFound.get())).isTrue();
        assertThat(oneTestUser.getBooks().size() == userFound.get().getBooks().size()).isTrue();
        assertThat(oneTestBook.getAuthor().equals(userFound.get().getBooks().get(0).getAuthor())).isTrue();
    }

    @Test
    public void whenFindByUsernameThatNotExist_thenReturnError() {
        Optional<User> userFound = userRepository.findByUsername("monkeys");
        assertThat(userFound.isPresent()).isFalse();
    }

    @Test
    public void whenFindAllByBirthDatesAndName_thenReturnUser() {
        Page<User> userFound = userRepository
                .findAllByNameIgnoreCaseContainingAndBirthdateBetween(oneTestUser.getBirthDate(),
                        oneTestUser.getBirthDate(),
                        oneTestUser.getName(), PageRequest.of(0, 5, Sort.by("id")));

        assertThat(userFound.getTotalElements() > 0).isTrue();
        assertFalse(userFound.get().findFirst().isEmpty());
        assertThat(oneTestUser.equals(userFound.get().findFirst().get())).isTrue();
    }

    @Test
    public void whenFindAllByBirthDatesAndName_thenReturnError() {
        Page<User> userFound = userRepository
                .findAllByNameIgnoreCaseContainingAndBirthdateBetween(oneTestUser.getBirthDate(),
                        oneTestUser.getBirthDate(),
                        "monkeys", PageRequest.of(0, 5, Sort.by("id")));

        assertThat(userFound.getTotalElements() == 0).isTrue();
        assertTrue(userFound.get().findFirst().isEmpty());
    }


}
