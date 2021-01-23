package wolox.training.models.dtos;

public class LoginDTO {

    private String username;

    private String password;

    public LoginDTO() {
        // Constructor For Jackson
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
