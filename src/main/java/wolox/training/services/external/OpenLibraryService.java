package wolox.training.services.external;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.dtos.BookInfoDTO;

@Service
public class OpenLibraryService {

    @Value("${external.api.url}")
    private String baseUrl;

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<BookInfoDTO> bookInfo(String isbn) {

        objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        String queryParam = "ISBN:" + isbn;
        String url = String.format(baseUrl, queryParam);

        BookInfoDTO bookInfo;

        try {
            JsonNode bookInfoJsonNode = objectMapper.readTree(restTemplate.getForObject(url, String.class));
            HashMap<String, Object> map = objectMapper.treeToValue(bookInfoJsonNode.get(queryParam), HashMap.class);
            bookInfo = objectMapper.convertValue(map, BookInfoDTO.class);

            if (bookInfo != null) {
                bookInfo.setIsbn(isbn);
            }

        } catch (IOException | RuntimeException e) {
            throw new BookNotFoundException();
        }

        return Optional.of(bookInfo);
    }
}
