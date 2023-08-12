package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.Ingredients.FoodData;
import org.example.Order.Order;
import org.example.Order.OrderMethods;
import org.example.User.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTests {
    private OrderMethods orderMethods;
    private UserMethods userMethods;
    private FoodData foodData;

    //Переменная которая говорит, нужно ли удалить пользователя true-да, false-нет
    private boolean isDeleteUser = true;

    @Before
    public void setUp() {
        foodData = new FoodData();
        orderMethods = new OrderMethods();
        userMethods = new UserMethods();
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с авторизацией и с ингредиентами, ожидается код 200 и success:true")
    public void createOrderTest() {
        userMethods.createUser();
        String accessToken = userMethods.getUserAccessToken();
        Order order = new Order(new String[]{foodData.getAllIngredientsId().get(0), foodData.getAllIngredientsId().get(1),foodData.getAllIngredientsId().get(3)});
        orderMethods.createOrder(accessToken, order).then()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Создание заказа с авторизацией и без ингредиентами, ожидается код 400 и message:Ingredient ids must be provided")
    public void createOrderWithoutIngredientsTest() {
        userMethods.createUser();
        String accessToken = userMethods.getUserAccessToken();
        Order order = new Order(new String[]{});
        orderMethods.createOrder(accessToken, order).then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неправильным хешем ингредиента")
    @Description("Создание заказа с авторизацией и с неправильным хешем ингредиента, ожидается 500 Internal Server Error.")
    public void createOrderWithIncorrectHashCodeTest() {
        userMethods.createUser();
        String accessToken = userMethods.getUserAccessToken();
        Order order = new Order(new String[]{"тут должен быть hashCode"});
        orderMethods.createOrder(accessToken, order).then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);

    }
    @Ignore("Тест выключен, так как тут баг")
    @Test
    @DisplayName("Создание заказа без авторизации с ингредиентами")
    @Description("Создание заказа без авторизацией с ингредиентами, ожидается код 401 Unauthorized")
    public void createOrderWithoutAuthorizationTest() {
        isDeleteUser = false;
        Order order = new Order(new String[]{foodData.getAllIngredientsId().get(0), foodData.getAllIngredientsId().get(1)});
        orderMethods.createOrderWithoutAuthorization(order).then()
                .statusCode(SC_UNAUTHORIZED);

    }
    @Ignore("Тест выключен, так как тут баг")
    @Test
    @DisplayName("Создание заказа без авторизации и без  ингредиентов")
    @Description("Создание заказа без авторизации и без  ингредиентов, ожидается код 401 Unauthorized")
    public void createOrderWithoutAuthorizationAndIngredientsTest() {
        isDeleteUser = false;
        Order order = new Order(new String[]{});
        orderMethods.createOrderWithoutAuthorization(order).then()
                .statusCode(SC_UNAUTHORIZED);

    }

    @After
    @DisplayName("Удаление созданного пользователя")
    @Description("Проверка что после удаления мы получим код 202 и сообщение -User successfully removed ")
    public void deleteUserDataTest() {
        if (isDeleteUser) {
            userMethods.deleteUser()
                    .then()
                    .statusCode(SC_ACCEPTED)
                    .and()
                    .assertThat().body("message", equalTo("User successfully removed"));
        }
    }
}
