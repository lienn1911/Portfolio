package project.partb;

public class UserSession {
    private static UserSession instance;

    private String username;
    private String email;

    private UserSession(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public static UserSession getInstance(String username, String email) {
        if (instance == null) {
            instance = new UserSession(username, email);
        }
        return instance;
    }

    public static UserSession getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void clearSession() {
        instance = null;
    }
}
