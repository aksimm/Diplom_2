package ru.yandex.praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class UserCreationTest {
    private User user = new User();
    private final UserClient client = new UserClient();
    private final UserGenerator generator = new UserGenerator();
    private String accessToken;

    @Before
    public void setUp() {
        user = generator.random();

    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void userCreation() {
        ValidatableResponse response = client.createUser(user);
        accessToken = response.extract().path("accessToken").toString();
        response
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание существующего пользователя")
    public void userCreationWithExistingEmail() {
        client.createUser(user);
        ValidatableResponse response = client.createUser(user);
        accessToken = response.extract().path("accessToken");
        response
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    public void userCreationWithoutEmail() {
        user.setEmail("");
        ValidatableResponse response = client.createUser(user);
        accessToken = response.extract().path("accessToken");
        response
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    public void userCreationWithoutPassword() {
        user.setPassword("");
        ValidatableResponse response = client.createUser(user);
        accessToken = response.extract().path("accessToken");
        response
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    public void userCreationWithoutName() {
        user.setName("");
        ValidatableResponse response = client.createUser(user);
        accessToken = response.extract().path("accessToken");
        response
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void deleteUser () {
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }
}
