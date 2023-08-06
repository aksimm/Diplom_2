package ru.yandex.praktikum.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.user.Credentials;
import ru.yandex.praktikum.user.User;
import ru.yandex.praktikum.user.UserClient;
import ru.yandex.praktikum.user.UserGenerator;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class OrderCreationTest {
    private User user = new User();
    private final UserClient client = new UserClient();
    private final OrderClient orderClient = new OrderClient();
    private final UserGenerator generator = new UserGenerator();
    private List<String> ingredients = new ArrayList<>();;
    Order order;
    private String accessToken;

    @Before
    public void setUp() {
        user = generator.random();
        client.createUser(user);
    }

    @Test
    @DisplayName("Cоздание заказа")
    public void orderCreation() {
        ValidatableResponse loginResponse = client.loginUser(Credentials.from(user));
        accessToken = loginResponse.extract().path("accessToken").toString();
        ingredients = orderClient.getIngredients().extract().path("data._id");
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.createOrder(order, accessToken);
        orderResponse
                     .assertThat()
                     .statusCode(200)
                     .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void orderCreationWithoutAuthorization() {
        ingredients = orderClient.getIngredients().extract().path("data._id");
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.createOrderWithoutLogin(order);
        orderResponse
                     .assertThat()
                     .statusCode(200)
                     .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void orderCreationWithoutIngredients() {
        ValidatableResponse loginResponse = client.loginUser(Credentials.from(user));
        accessToken = loginResponse.extract().path("accessToken").toString();
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.createOrder(order, accessToken);
        orderResponse
                     .assertThat()
                     .statusCode(400)
                     .body("success", is(false))
                     .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void orderCreationWithWrongHashOfIngredients() {
        ValidatableResponse loginResponse = client.loginUser(Credentials.from(user));
        accessToken = loginResponse.extract().path("accessToken").toString();
        ingredients.add("60d3b41abdacab0026a733c6123456789");
        ingredients.add("60d3b41abdacab0026a733c6qwerty1234");
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.createOrder(order, accessToken);
        orderResponse
                     .assertThat()
                     .statusCode(500);
    }

    @After
    public void deleteUser () {
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }
}
