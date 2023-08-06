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

public class GetOrderTest {
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
    @DisplayName("Получение заказа авторизованного пользователя")
    public void getOrderOfUserWithLogin() {
        ValidatableResponse loginResponse = client.loginUser(Credentials.from(user));
        accessToken = loginResponse.extract().path("accessToken").toString();
        ingredients = orderClient.getIngredients().extract().path("data._id");
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.getOrdersWithAuthorization(accessToken);
        orderResponse
                     .assertThat()
                     .statusCode(200)
                     .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказа авторизованного пользователя")
    public void getOrderOfUserWithoutLogin() {
        ingredients = orderClient.getIngredients().extract().path("data._id");
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderClient.getOrdersWithoutAuthorization();
        orderResponse
                     .assertThat()
                     .statusCode(401)
                     .body("success", equalTo(false))
                     .body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser () {
        if (accessToken != null) {
            client.deleteUser(accessToken);
        }
    }
}
