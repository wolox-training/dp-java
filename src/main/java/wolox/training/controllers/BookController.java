package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.dtos.BookInfoDTO;
import wolox.training.repositories.BookRepository;
import wolox.training.services.external.OpenLibraryService;

@RestController
@RequestMapping("/api/books")
@Api(tags = "Books")
public class BookController {

    private final BookRepository bookRepository;
    private final OpenLibraryService openLibraryService;

    @Autowired
    public BookController(BookRepository bookRepository,
            OpenLibraryService openLibraryService) {
        this.bookRepository = bookRepository;
        this.openLibraryService = openLibraryService;
    }

    /**
     * This method returns all the books stored in the database through a filter
     *
     * @return {@link List<Book>}
     */
    @GetMapping
    @ApiOperation(value = "Given a filter type and a param for filter, return all books", response = Book[].class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Not Authorized"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public List<Book> findAllByPublisherOrGenreOrYear(@RequestParam(name = "publisher", required = false) String publisher,
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "year", required = false) String year) {
        return bookRepository.getAllBook(publisher, genre, year);
    }

    /**
     * This method consults book by author
     *
     * @param bookAuthor: is the author of the book
     * @return {@link Book}
     */
    @GetMapping("/author/{bookAuthor}")
    @ApiOperation(value = "Giving an author, return one book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Not Authorized"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "Book Not Found"),
    })
    public Book findByAuthor(@ApiParam(value = "author to find the book") @PathVariable String bookAuthor) {
        return bookRepository.findByAuthor(bookAuthor).orElseThrow(BookNotFoundException::new);
    }

    /**
     * This method returns a book per id
     *
     * @param id: this is the unique identifier generated by the database
     * @return {@link Book}
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Giving an id, return one book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Not Authorized"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "Resource not found")
    })
    public Book findOne(@ApiParam(value = "id to find the book") @PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    /**
     * This method is used to save a book
     *
     * @param book: receives the structure or book model
     * @return {@link Book}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Creates a book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book successfully created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Book create(@ApiParam(value = "body of the book") @RequestBody Book book) {
        return bookRepository.save(book);
    }

    /**
     * This method removes a book by its id
     *
     * @param id: this is the unique identifier generated by the database
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes a book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted book"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public void delete(@ApiParam(value = "id to delete the book") @PathVariable Long id) {
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    /**
     * This method updates a book but validates that this book exists and also validates the ids
     *
     * @param book: receives the structure or book model
     * @param id:   this is the unique identifier generated by the database
     * @return {@link Book}
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Updates a book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated book"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Book updateBook(@ApiParam(value = "body of the book") @RequestBody Book book,
            @ApiParam(value = "id to find the book") @PathVariable Long id) {
        if (!book.getId().equals(id)) {
            throw new BookIdMismatchException();
        }

        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

    /**
     * Given the isbn of a book, search the database or an external service and return the book or a book exception was
     * not found
     *
     * @param isbn: this is the book identification
     * @return {@link Book}
     */
    @GetMapping("/isbn/{isbn}")
    @ApiOperation(value = "Given the isbn of a book, search the database or an external service and return the book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 201, message = "Book successfully created"),
            @ApiResponse(code = 401, message = "Not Authorized"),
            @ApiResponse(code = 403, message = "Access forbidden"),
            @ApiResponse(code = 404, message = "Book Not Found"),
    })
    public ResponseEntity<Book> findByIsbn(
            @ApiParam(value = "this is the book identification") @PathVariable(name = "isbn") String isbn) {
        Optional<Book> bookOptional = bookRepository.findByIsbn(isbn);

        if (bookOptional.isEmpty()) {
            BookInfoDTO bookInfoDTO = openLibraryService.bookInfo(isbn).orElseThrow(BookNotFoundException::new);
            return new ResponseEntity<>(bookRepository.save(new Book(bookInfoDTO)), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
    }

    @GetMapping
    public Page<Book> findAll(
            @RequestParam(required = false, defaultValue = "") String publisher,
            @RequestParam(required = false, defaultValue = "") String genre,
            @RequestParam(required = false, defaultValue = "") String year,
            @RequestParam(required = false, defaultValue = "") String image,
            @RequestParam(required = false, defaultValue = "") String author,
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") String subtitle,
            @RequestParam(required = false, defaultValue = "0") Integer pages,
            @RequestParam(required = false, defaultValue = "") String isbn,
            Pageable pageable) {
        return bookRepository.findAllBooks(publisher, genre, year, author, image, title, subtitle, pages,
                isbn, pageable);

    }

}
