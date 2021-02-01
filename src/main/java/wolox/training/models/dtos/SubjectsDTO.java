package wolox.training.models.dtos;

public class SubjectsDTO {

    private String name;

    private String url;

    public SubjectsDTO() {
        // Constructor For Jackson
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
