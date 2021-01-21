package wolox.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

    public static final String API_BOOKS = "/api/books/";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository mockedBookRepository;

    private Book oneTestBook;
    private Book oneTestBookCreated;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        oneTestBook = new Book();
        oneTestBookCreated = new Book(1L);

        oneTestBook.setGenre("Fantasy");
        oneTestBook.setAuthor("John Ronald Reuel Tolkien");
        oneTestBook.setImage("https://images-na.ssl-images-amazon.com/images/I/81U5RVCuTHL.jpg");
        oneTestBook.setTitle("The Lord of the Rings");
        oneTestBook.setSubTitle("The Two Towers");
        oneTestBook.setPublisher("George Allen & Unwin");
        oneTestBook.setYear("1954");
        oneTestBook.setPages(352);
        oneTestBook.setIsbn("PR6039.O32 L6 1954, v.2");

        oneTestBookCreated.setGenre("Fantasy");
        oneTestBookCreated.setAuthor("John Ronald Reuel Tolkien");
        oneTestBookCreated.setImage("https://images-na.ssl-images-amazon.com/images/I/81U5RVCuTHL.jpg");
        oneTestBookCreated.setTitle("The Lord of the Rings");
        oneTestBookCreated.setSubTitle("The Two Towers");
        oneTestBookCreated.setPublisher("George Allen & Unwin");
        oneTestBookCreated.setYear("1954");
        oneTestBookCreated.setPages(352);
        oneTestBookCreated.setIsbn("PR6039.O32 L6 1954, v.2");
    }

    @Test
    void whenFindByAllWhichExist_thenBooksIsReturned() throws Exception {
        String jsonBooks = mapper.writeValueAsString(Collections.singletonList(oneTestBook));
        Mockito.when(mockedBookRepository.getAllBook(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.singletonList(oneTestBook));

        String url = API_BOOKS;
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBooks));
    }

    @Test
    public void whenFindByAuthorWhichExist_thenBookIsReturned() throws Exception {
        String jsonBook = mapper.writeValueAsString(oneTestBook);
        Mockito.when(mockedBookRepository.findByAuthor(oneTestBook.getAuthor())).thenReturn(Optional.of(oneTestBook));

        String url = API_BOOKS.concat("author/").concat(oneTestBook.getAuthor());
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBook));
    }

    @Test
    public void whenFindByAuthorWhichNoExist_thenReturnNotFound() throws Exception {
        Mockito.when(mockedBookRepository.findByAuthor(Mockito.anyString())).thenThrow(BookNotFoundException.class);

        String url = API_BOOKS.concat("author/").concat(oneTestBook.getAuthor());
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindOneWhichExist_thenBookIsReturned() throws Exception {
        String jsonBook = mapper.writeValueAsString(oneTestBook);
        Mockito.when(mockedBookRepository.findById(1L)).thenReturn(Optional.of(oneTestBook));

        String url = API_BOOKS.concat(String.valueOf(1L));
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBook));
    }

    @Test
    public void whenCreateBook_thenBookIsReturned() throws Exception {
        String jsonBook = mapper.writeValueAsString(oneTestBook);
        String jsonBookCreated = mapper.writeValueAsString(oneTestBookCreated);
        Mockito.when(mockedBookRepository.save(Mockito.any())).thenReturn(oneTestBookCreated);

        String url = API_BOOKS;
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonBookCreated));
    }

    @Test
    public void whenDeleteBook_thenStatusOkReturned() throws Exception {
        Mockito.when(mockedBookRepository.findById(Mockito.any())).thenReturn(Optional.of(oneTestBook));

        String url = API_BOOKS.concat(String.valueOf(1L));
        mvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateBook_thenBookIsReturned() throws Exception {
        String jsonBookCreated = mapper.writeValueAsString(oneTestBookCreated);
        Mockito.when(mockedBookRepository.findById(Mockito.any())).thenReturn(Optional.of(oneTestBookCreated));
        Mockito.when(mockedBookRepository.save(Mockito.any())).thenReturn(oneTestBookCreated);

        String url = API_BOOKS.concat(String.valueOf(1L));
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBookCreated))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBookCreated));
    }

}
