package wolox.training.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public UserController(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * This method returns all the users stored in the database
     *
     * @return {@link List<User>}
     */
    @GetMapping
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * This method consults book by name
     *
     * @param username: is the username
     * @return {@link User}
     */
    @GetMapping("/username/{username}")
    public User findByName(@PathVariable String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    /**
     * This method returns a user per id
     *
     * @param id: this is the unique identifier generated by the database
     * @return {@link Book}
     */
    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    /**
     * This method is used to save a user
     *
     * @param user: receives the structure or user model
     * @return {@link User}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * This method removes a book by its id
     *
     * @param id: this is the unique identifier generated by the database
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    /**
     * This method updates a user but validates that this user exists and also validates the ids
     *
     * @param user: receives the structure or user model
     * @param id:   this is the unique identifier generated by the database
     * @return {@link User}
     */
    @PutMapping("/{id}")
    public User updateBook(@RequestBody User user, @PathVariable Long id) {
        if (!user.getId().equals(id)) {
            throw new UserIdMismatchException();
        }

        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }

    /**
     * This method adds a book to a specific user
     *
     * @param userId: this is the unique identifier of the user
     * @param bookId: this is the unique identifier of the book
     * @return {@link User}
     */
    @PostMapping("/{userId}/books/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public User addBookToUser(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);

        user.addBook(book);
        return userRepository.save(user);
    }

    /**
     * This method removes a workbook associated with a specific user
     *
     * @param userId: this is the unique identifier of the user
     * @param bookId: this is the unique identifier of the book
     * @return {@link User}
     */
    @DeleteMapping("/{userId}/books/{bookId}")
    public User deleteBookToUser(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);

        user.deleteBook(book);
        return userRepository.save(user);
    }
}
