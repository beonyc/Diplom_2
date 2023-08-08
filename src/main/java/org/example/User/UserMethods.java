package org.example.User;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserMethods {
    private final String BASE_URL = "https://stellarburgers.nomoreparties.site/api/";

    private User user;
    private Response userDataResponse;

    public UserMethods() {
        user = new User();
    }

    public void setEmailForUser(String email) {
        user.setEmail(email);
    }

    public Response getUserDataResponse() {
        return userDataResponse;
    }


    @Step("Создание пользователя")
    public Response createUser() {
        Response response = given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(user)
                .post("auth/register");
        userDataResponse = response;

        return response;
    }

    @Step("Создание пользователя без поля email")
    public Response createUserWithoutEmail() {
        return given().baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(user.getUserWithoutEmail())
                .post("auth/register");
    }

    @Step("Авторизация пользователя")
    public Response userAuthorization() {
        return given().baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(user)
                .post("auth/login");

    }

    @Step("Авторизация пользователя с неверным логином и паролем")
    public Response userAuthorizationWithIncorrectData() {
        user.setEmail("someNewEmail666@yandex.ru");
        user.setPassword("newPassword666");
        return given().baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(user)
                .post("auth/login");

    }

    @Step("Изменение данных пользователя")
    public Response changeUserInformation(String token, User userData) {
        return given().baseUri(BASE_URL)
                .auth().oauth2(token)
                .header("Content-Type", "application/json")
                .body(userData)
                .patch("auth/user");
    }

    @Step("Изменение имени пользователя без авторизации")
    public Response changeUserInformationWithoutAuthorization() {
        User userNameToChange = new User(null, null, "newName");
        return given().baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(userNameToChange)
                .patch("auth/user");
    }

    @Step("Получние accessToken пользователя")
    public String getUserAccessToken() {
        Response response = given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(user)
                .post("auth/login");
        String accessToken = response.then().extract().path("accessToken");
        return accessToken.replace("Bearer ", "");
    }

    @Step("Удаление пользователя")
    public Response deleteUser() {
        return given()
                .baseUri(BASE_URL)
                .auth().oauth2(getUserAccessToken())
                .delete("auth/user");
    }
}
