package wolox.training.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class BookInfoDTO {

    private String isbn;

    private String title;

    private String subtitle;

    private List<PublisherDTO> publishers;

    @JsonProperty("publish_date")
    private String publishDate;

    @JsonProperty("number_of_pages")
    private Integer numberPages;

    private List<AuthorDTO> authors;

    @JsonIgnore
    private String url;

    public BookInfoDTO() {
        // Constructor For Jackson
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<PublisherDTO> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<PublisherDTO> publishers) {
        this.publishers = publishers;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getNumberPages() {
        return numberPages;
    }

    public void setNumberPages(Integer numberPages) {
        this.numberPages = numberPages;
    }

    public List<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDTO> authors) {
        this.authors = authors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
