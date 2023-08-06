package ru.yandex.praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.Client;

public class OrderClient extends Client {
    private final static String ORDERS_API = "/orders";
    private final static String INGREDIENTS_API = "/ingredients";

    @Step("Создание заказа c авторизацией")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return  spec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_API)
                .then().log().all();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutLogin(Order order) {
        return  spec()
                .body(order)
                .when()
                .post(ORDERS_API)
                .then().log().all();
    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return spec()
                .log().uri()
                .when()
                .get(INGREDIENTS_API)
                .then().log().all();
    }

    @Step("Получение заказов конкретного пользователя с авторизацией")
    public ValidatableResponse getOrdersWithAuthorization(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .get(ORDERS_API)
                .then().log().all();
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public ValidatableResponse getOrdersWithoutAuthorization() {
        return spec()
                .get(ORDERS_API)
                .then().log().all();
    }


}
