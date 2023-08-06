package ru.yandex.praktikum.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
public class UserUpdateTest {
    private User user = new User();
    private final UserClient client = new UserClient();
    private final UserGenerator generator = new UserGenerator();
    private String accessToken;

    @Before
    public void setUp() {
        user = generator.random();
    }

    @Test
    @DisplayName("Изменение информации пользователя")
    public void userUpdate() {
        ValidatableResponse response = client.createUser(user);
        accessToken = response.extract().path("accessToken").toString();
        user.setName("John");
        user.setEmail("new" + user.getEmail());
        ValidatableResponse loginResponse = client.updateUser(user, accessToken);
        loginResponse
                     .assertThat()
                     .statusCode(200)
                     .body("success", is(true))
                     .body("user.name", equalTo(user.getName()))
                     .body("user.email", equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Изменение информации пользователя без авторизации")
    public void userUpdateWithoutAuthorization() {
        user.setName("John");
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@user.com");
        ValidatableResponse response = client.updateUserWithoutLogin(user);
        response
                .assertThat()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser () {
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }
}
