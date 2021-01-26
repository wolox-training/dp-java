package wolox.training.models.dtos;

public class AuthorDTO {

    public String name;

    public AuthorDTO() {
        // Constructor For Jackson
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
