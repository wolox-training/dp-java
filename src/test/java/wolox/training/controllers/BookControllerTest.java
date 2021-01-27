package wolox.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookControllerTest {

    public static final String API_BOOKS = "/api/books/";

    @Autowired
    private WebApplicationContext context;

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
        oneTestBook.setGenre("Fantasy");
        oneTestBook.setAuthor("John Ronald Reuel Tolkien");
        oneTestBook.setImage("https://images-na.ssl-images-amazon.com/images/I/81U5RVCuTHL.jpg");
        oneTestBook.setTitle("The Lord of the Rings");
        oneTestBook.setSubTitle("The Two Towers");
        oneTestBook.setPublisher("George Allen & Unwin");
        oneTestBook.setYear("1954");
        oneTestBook.setPages(352);
        oneTestBook.setIsbn("PR6039.O32 L6 1954, v.2");

        oneTestBookCreated = new Book(1L);
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

    @WithMockUser
    @Test
    void whenFindByAllWhichExist_thenBooksIsReturned() throws Exception {
        String jsonBooks = mapper.writeValueAsString(Collections.singletonList(oneTestBook));
        Mockito.when(mockedBookRepository.getAllBooksMatch(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.singletonList(oneTestBook));

        mvc.perform(get(API_BOOKS)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBooks));
    }

    @WithMockUser
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

    @WithMockUser
    @Test
    public void whenFindByAuthorWhichNoExist_thenReturnNotFound() throws Exception {
        Mockito.when(mockedBookRepository.findByAuthor(Mockito.anyString())).thenThrow(BookNotFoundException.class);

        String url = API_BOOKS.concat("author/").concat(oneTestBook.getAuthor());
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser
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

    @WithMockUser
    @Test
    public void whenCreateBook_thenBookIsReturned() throws Exception {
        String jsonBook = mapper.writeValueAsString(oneTestBook);
        String jsonBookCreated = mapper.writeValueAsString(oneTestBookCreated);
        Mockito.when(mockedBookRepository.save(Mockito.any())).thenReturn(oneTestBookCreated);

        mvc.perform(post(API_BOOKS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonBookCreated));
    }

    @WithMockUser
    @Test
    public void whenDeleteBook_thenStatusOkReturned() throws Exception {
        Mockito.when(mockedBookRepository.findById(Mockito.any())).thenReturn(Optional.of(oneTestBook));

        String url = API_BOOKS.concat(String.valueOf(1L));
        mvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser
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
