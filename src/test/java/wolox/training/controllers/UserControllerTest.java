package wolox.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application-test.properties"})
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

    public static final String API_USERS = "/api/users/";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository mockedUserRepository;

    @MockBean
    private BookRepository mockedBookRepository;

    private User oneTestUser;
    private User oneTestUserCreated;
    private Book oneTestBookCreated;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        oneTestUser = new User();
        oneTestUser.setUsername("daniel.delapava");
        oneTestUser.setName("Daniel De La Pava Suarez");
        oneTestUser.setPassword("password");
        oneTestUser.setBirthDate(LocalDate.of(1989, 10, 16));

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

        oneTestUserCreated = new User(1L);
        oneTestUserCreated.setUsername("daniel.delapava");
        oneTestUserCreated.setName("Daniel De La Pava Suarez");
        oneTestUserCreated.setPassword("password");
        oneTestUserCreated.setBirthDate(LocalDate.of(1989, 10, 16));
        oneTestUserCreated.setBooks(new LinkedList<>(Collections.singletonList(oneTestBookCreated)));

    }

    @WithMockUser
    @Test
    void whenFindByAllWhichExist_thenUsersIsReturned() throws Exception {
        String jsonUsers = mapper.writeValueAsString(Collections.singletonList(oneTestUser));
        Mockito.when(mockedUserRepository.findAll()).thenReturn(Collections.singletonList(oneTestUser));

        mvc.perform(get(API_USERS)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUsers));
    }

    @WithMockUser
    @Test
    public void whenFindByUsernameWhichExist_thenUserIsReturned() throws Exception {
        String jsonUser = mapper.writeValueAsString(oneTestUser);
        Mockito.when(mockedUserRepository.findByUsername(oneTestUser.getUsername()))
                .thenReturn(Optional.of(oneTestUser));

        String url = API_USERS.concat("username/").concat(oneTestUser.getUsername());
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUser));
    }

    @WithMockUser
    @Test
    public void whenFindByUsernameWhichNoExist_thenReturnNotFound() throws Exception {
        Mockito.when(mockedUserRepository.findByUsername(Mockito.anyString())).thenThrow(UserNotFoundException.class);

        String url = API_USERS.concat("username/").concat(oneTestUser.getUsername());
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    public void whenFindOneWhichExist_thenUserIsReturned() throws Exception {
        String jsonUser = mapper.writeValueAsString(oneTestUser);
        Mockito.when(mockedUserRepository.findById(1L)).thenReturn(Optional.of(oneTestUser));

        String url = API_USERS.concat(String.valueOf(1L));
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUser));
    }

    @WithMockUser
    @Test
    public void whenCreateUser_thenUserIsReturned() throws Exception {
        String jsonUser = mapper.writeValueAsString(oneTestUser);
        String jsonUserCreated = mapper.writeValueAsString(oneTestUserCreated);
        Mockito.when(mockedUserRepository.save(Mockito.any())).thenReturn(oneTestUserCreated);

        mvc.perform(post(API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonUserCreated));
    }

    @WithMockUser
    @Test
    public void whenDeleteBook_thenStatusOkReturned() throws Exception {
        Mockito.when(mockedUserRepository.findById(Mockito.any())).thenReturn(Optional.of(oneTestUser));

        String url = API_USERS.concat(String.valueOf(1L));
        mvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void whenUpdateBook_thenBookIsReturned() throws Exception {
        String jsonUserCreated = mapper.writeValueAsString(oneTestUserCreated);
        Mockito.when(mockedUserRepository.findById(Mockito.any())).thenReturn(Optional.of(oneTestUserCreated));
        Mockito.when(mockedUserRepository.save(Mockito.any())).thenReturn(oneTestUserCreated);

        String url = API_USERS.concat(String.valueOf(1L));
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUserCreated))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUserCreated));
    }

    @WithMockUser
    @Test
    public void whenAUserAddBook_thenUserIsReturned() throws Exception {
        String jsonUserCreated = mapper.writeValueAsString(oneTestUserCreated);

        Mockito.when(mockedBookRepository.findById(Mockito.any())).thenReturn(Optional.of(oneTestBookCreated));
        Mockito.when(mockedUserRepository.findById(Mockito.any())).thenReturn(Optional.of(oneTestUser));
        Mockito.when(mockedUserRepository.save(Mockito.any())).thenReturn(oneTestUserCreated);

        String url = API_USERS.concat(String.valueOf(oneTestUserCreated.getId())).concat("/books/")
                .concat(String.valueOf(oneTestBookCreated.getId()));
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonUserCreated));
    }

    @WithMockUser
    @Test
    public void whenAUserDeleteBook_thenUserIsReturned() throws Exception {
        String jsonUserCreated = mapper.writeValueAsString(oneTestUser);

        Mockito.when(mockedBookRepository.findById(Mockito.any())).thenReturn(Optional.of(oneTestBookCreated));
        Mockito.when(mockedUserRepository.findById(Mockito.any())).thenReturn(Optional.of(oneTestUserCreated));
        Mockito.when(mockedUserRepository.save(Mockito.any())).thenReturn(oneTestUser);

        String url = API_USERS.concat(String.valueOf(oneTestUserCreated.getId())).concat("/books/")
                .concat(String.valueOf(oneTestBookCreated.getId()));
        mvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUserCreated));
    }
}
