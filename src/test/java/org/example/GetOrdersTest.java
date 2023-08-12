package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.Order.OrderMethods;
import org.example.User.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class GetOrdersTest {
    private UserMethods userMethods;
    private OrderMethods orderMethods;
    //Переменная которая говорит, нужно ли удалить пользователя true-да, false-нет
    private boolean isDeleteUser = true;

    @Before
    public void setUp() {
        userMethods = new UserMethods();
        orderMethods = new OrderMethods();
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Получение заказов авторизованного пользователя через его accessToken")
    public void getOrdersOfParticularUserTest() {
        userMethods.createUser();
        String accessToken = userMethods.getUserAccessToken();
        Response response = orderMethods.getOrderListOfParticularUser(accessToken);
        response.then()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Получение заказов неавторизованного пользователя, ожидается код 401 и message: You should be authorised")
    public void getOrdersOfParticularUnauthorizedUserTest() {
        isDeleteUser =false;
        Response response = orderMethods.getOrdersOfParticularUnauthorizedUser();
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("message", equalTo("You should be authorised"));
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
