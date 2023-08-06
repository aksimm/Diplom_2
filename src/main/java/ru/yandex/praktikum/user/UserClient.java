package ru.yandex.praktikum.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.Client;

public class UserClient extends Client {
    private final static String NEW_USER_API = "/auth/register";
    private final static String LOGIN_API = "/auth/login";
    private final static String AUTH_USER_API = "/auth/user";

    @Step("Регистрация пользователя")
    public ValidatableResponse createUser(User user) {
        return spec()
                .body(user)
                .when()
                .post(NEW_USER_API)
                .then().log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(Credentials credentials) {
        return spec()
                .body(credentials)
                .when()
                .post(LOGIN_API)
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .delete(AUTH_USER_API)
                .then();
    }

    @Step("Изменение пользователя c авторизацией")
    public ValidatableResponse updateUser(User user, String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(AUTH_USER_API)
                .then().log().all();
    }

    @Step("Изменение пользователя без авторизации")
    public ValidatableResponse updateUserWithoutLogin(User user) {
        return spec()
                .body(user)
                .when()
                .patch(AUTH_USER_API)
                .then().log().all();
    }




}
