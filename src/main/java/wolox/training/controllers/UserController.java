package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.models.dtos.LoginDTO;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.security.CustomAuthProvider;

@RestController
@RequestMapping("/api/users")
@Api(tags = "Users")
public class UserController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CustomAuthProvider customAuthProvider;

    public UserController(UserRepository userRepository, BookRepository bookRepository,
            CustomAuthProvider customAuthProvider) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.customAuthProvider = customAuthProvider;
    }

    /**
     * This method returns all the users stored in the database
     *
     * @return {@link List<User>}
     */
    @GetMapping
    @ApiOperation(value = "return all users", response = User[].class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Not Authorized"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "User Not Found")
    })
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
    @ApiOperation(value = "Giving an username, return one user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Not Authorized"),
            @ApiResponse(code = 403, message = "Access forbidden")
    })
    public User findByUsername(@ApiParam(value = "username to find the user") @PathVariable String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    /**
     * This method returns a user per id
     *
     * @param id: this is the unique identifier generated by the database
     * @return {@link Book}
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Giving an id, return one user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Not Authorized"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "User Not Found")
    })
    public User findOne(@ApiParam(value = "id to find the user") @PathVariable Long id) {
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
    @ApiOperation(value = "Creates a user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User successfully created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public User create(@ApiParam(value = "body of the user") @RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * This method removes a book by its id
     *
     * @param id: this is the unique identifier generated by the database
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted user"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public void delete(@ApiParam(value = "id to delete the user") @PathVariable Long id) {
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
    @ApiOperation(value = "Updates a user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public User updateUser(@ApiParam(value = "body of the user") @RequestBody User user,
            @ApiParam(value = "id to find the user") @PathVariable Long id) {
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
    @ApiOperation(value = "Given the id of a user and the id of a book, the book is added to the user, returns the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public User addBookToUser(@ApiParam(value = "id to find the user") @PathVariable Long userId,
            @ApiParam(value = "id to find the book") @PathVariable Long bookId) {
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
    @ApiOperation(value = "Given the id of a user and the id of a book, the book is deleted to the user, returns the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public User deleteBookToUser(@ApiParam(value = "id to find the user") @PathVariable Long userId,
            @ApiParam(value = "id to delete the book") @PathVariable Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);

        user.deleteBook(book);
        return userRepository.save(user);
    }

    @PatchMapping("/{id}/password")
    @ApiOperation(value = "Given the id of the user, the password of the user will be updated, return the user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password user updated"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> updatePassword(@RequestBody LoginDTO loginDTO, @PathVariable Long id) {
        User userFounded = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userFounded.setPassword(loginDTO.getPassword());
        userRepository.save(userFounded);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Given the username of a user, return the user logged", response = User.class)
    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully login user"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    public User login(@RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                loginDTO.getPassword());

        Authentication auth = customAuthProvider.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();

        securityContext.setAuthentication(auth);

        return userRepository.findByUsername(loginDTO.getUsername()).orElseThrow(UserNotFoundException::new);
    }



    /**
     * This method obtains a list of users with some parameters
     *
     * @param startDate: Start date as first date in the range search
     * @param endDate:   End date as last date in the range search
     * @param sequence:  The sequence contains the characters must contain the user's name
     * @param from:      Where the results page starts
     * @param size:      List is the size of the expected result
     * @param sort:      It is the field by which you want to order
     * @return {@link Page<User>}
     */
    @GetMapping("/search")
    @ApiOperation(value = "Giving a start date birthday, an end date birthday and a sequence of characters of the user's name, returns the users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Not Authorized"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "User Not Found")
    })
    public Page<User> findUsers(
            @ApiParam(value = "start date as first date in the range search") @RequestParam(name = "startDate", required = false) String startDate,
            @ApiParam(value = "end date as last date in the range search") @RequestParam(name = "endDate", required = false) String endDate,
            @ApiParam(value = "The sequence contains the characters must contain the user's name") @RequestParam(name = "sequence", required = false, defaultValue = "") String sequence,
            @ApiParam(value = "where the results page starts") @RequestParam(name = "from", defaultValue = "0") Integer from,
            @ApiParam(value = "list is the size of the expected result") @RequestParam(name = "size", defaultValue = "5") Integer size,
            @ApiParam(value = "it is the field by which you want to order") @RequestParam(name = "sort", defaultValue = "id") String sort

    ) {
        return userRepository.findAllByNameIgnoreCaseContainingAndBirthdateBetween(
                Objects.nonNull(startDate) ? LocalDate.parse(startDate) : null,
                Objects.nonNull(endDate) ? LocalDate.parse(endDate) : null,
                sequence, PageRequest.of(from, size, Sort.by(sort)));
    }

}
