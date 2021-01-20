package wolox.training.models;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public Long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
