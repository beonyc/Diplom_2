package org.example.Order;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderMethods {
    private final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/";


    @Step("Создание заказа с авторизацией")
    public Response createOrder(String token,Order order){
        return given().baseUri(BASE_URL)
                .auth().oauth2(token)
                .header("Content-Type", "application/json")
                .body(order)
                .post("orders");
    }
    @Step("Создание заказа без авторизацией")
    public Response createOrderWithoutAuthorization(Order order){
        return given().baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(order)
                .post("orders");
    }
    @Step("Получение заказов пользователя")
    public Response getOrderListOfParticularUser(String accessToken){
        return given().baseUri(BASE_URL)
                .auth().oauth2(accessToken)
                .get("orders");

    }
    @Step("Получение заказов без токена пользователя")
    public Response getOrdersOfParticularUnauthorizedUser(){
        return given().baseUri(BASE_URL)
                .get("orders");

    }
}
