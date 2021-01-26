package wolox.training.models;

import com.google.common.base.Preconditions;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.apache.commons.lang3.StringUtils;
import wolox.training.models.constans.ErrorConstants;
import wolox.training.models.dtos.BookInfoDTO;

/**
 * Represents struct a book.
 *
 * @author Daniel De La Pava
 */
@Entity
@ApiModel(description = "Book from data base")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ApiModelProperty(notes = "The book genre: could be horror, comedy, drama, etc.", required = true)
    @NotNull
    private String genre;

    @ApiModelProperty(notes = "The book author: this is the author of the book", required = true)
    @NotNull
    private String author;

    @ApiModelProperty(notes = "The book image: this is url the image the book", required = true)
    @NotNull
    private String image;

    @ApiModelProperty(notes = "The book title: this is the title the book", required = true)
    @NotNull
    private String title;

    @ApiModelProperty(notes = "The book subtitle: this is the subtitle the book", required = true)
    @NotNull
    private String subTitle;

    @ApiModelProperty(notes = "The book publisher: this is the publisher the book", required = true)
    @NotNull
    private String publisher;

    @ApiModelProperty(notes = "The book year: this is the year the book", required = true)
    @NotNull
    private String year;

    @ApiModelProperty(notes = "The book page: this is the page the book", required = true)
    @NotNull
    private Integer pages;

    /**
     * Represents the International Standard Book Number.
     */
    @ApiModelProperty(notes = "The book isbn: this is the book identification", required = true)
    @NotNull
    private String isbn;

    public Book() {
        //Constructor for JPA
    }

    public Book(BookInfoDTO bookInfoDTO) {
        this.genre = "N/A - External API";
        this.author = bookInfoDTO.getAuthors().get(0).getName();
        this.image = bookInfoDTO.getUrl();
        this.title = bookInfoDTO.getTitle();
        this.subTitle = bookInfoDTO.getSubtitle();
        this.publisher = bookInfoDTO.getPublishers().get(0).getName();
        this.year = bookInfoDTO.getPublishDate();
        this.pages = bookInfoDTO.getNumberPages();
        this.isbn = bookInfoDTO.getIsbn();
    }

    public Book(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        Preconditions.checkNotNull(genre, String.format(ErrorConstants.NOT_NULL, "genre"));
        Preconditions.checkArgument(!genre.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, "genre"));

        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        Preconditions.checkNotNull(author, String.format(ErrorConstants.NOT_NULL, "author"));
        Preconditions.checkArgument(!author.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, "author"));

        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        Preconditions.checkNotNull(image, String.format(ErrorConstants.NOT_NULL, "image"));
        Preconditions.checkArgument(!image.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, "image"));

        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Preconditions.checkNotNull(title, String.format(ErrorConstants.NOT_NULL, "title"));
        Preconditions.checkArgument(!title.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, "title"));

        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        Preconditions.checkNotNull(subTitle, String.format(ErrorConstants.NOT_NULL, "subTitle"));

        this.subTitle = subTitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        Preconditions.checkNotNull(publisher, String.format(ErrorConstants.NOT_NULL, "publisher"));
        Preconditions.checkArgument(!publisher.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, "publisher"));

        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        Preconditions.checkNotNull(year, String.format(ErrorConstants.NOT_NULL, "year"));
        Preconditions.checkArgument(StringUtils.isNumeric(year), String.format(ErrorConstants.NOT_NUMERIC, "year"));
        Preconditions.checkArgument(Integer.parseInt(year) > 0, String.format(ErrorConstants.NOT_GREATER_THAN, "year", "0"));

        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        Preconditions.checkNotNull(pages, String.format(ErrorConstants.NOT_NULL, "pages"));
        Preconditions.checkArgument(pages > 0, String.format(ErrorConstants.NOT_GREATER_THAN, "pages", "0"));

        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        Preconditions.checkNotNull(isbn, String.format(ErrorConstants.NOT_NULL, "isbn"));
        Preconditions.checkArgument(!isbn.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, "isbn"));

        this.isbn = isbn;
    }
}
