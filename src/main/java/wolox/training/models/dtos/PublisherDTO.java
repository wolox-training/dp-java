package wolox.training.models.dtos;

public class PublisherDTO {

    public String name;

    public PublisherDTO() {
        // Constructor For Jackson
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
