package ru.yandex.praktikum.user;

public class Credentials {
    private String email;
    private String password;

    public Credentials() {
    }

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Credentials from(User user) {
        Credentials c = new Credentials();
        c.setEmail(user.getEmail());
        c.setPassword(user.getPassword());
        return c;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
