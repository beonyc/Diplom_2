package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.User.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.Matchers.equalTo;

public class AuthUserTests {
    private UserMethods userMethods;
    //Переменная которая говорит, нужно ли удалить пользователя true-да, false-нет
    private boolean isDeleteUser =true;

    @Before
    public void setUp() {
        userMethods = new UserMethods();
    }
    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Проверка что при корректной авторизации получим код 200 и success: true")
    public void userAuthTest(){
        userMethods.createUser();
        userMethods.userAuthorization().then()
                .statusCode(200)
                .and()
                .assertThat().body("success",equalTo(true));
    }
    @Test
    @DisplayName("Авторизация пользователя с неверным логином и паролем ")
    @Description("Проверка что при некорректной авторизации получим код 401 и message:email or password are incorrect")
    public void userAuthWithIncorrectDataTest(){
        isDeleteUser =false;
        userMethods.userAuthorizationWithIncorrectData().then()
                .statusCode(401)
                .and()
                .assertThat().body("message",equalTo("email or password are incorrect"));

    }


    @After
    @DisplayName("Удаление созданного пользователя")
    @Description("Проверка что после удаления мы получим код 202 и сообщение -User successfully removed ")
    public void deleteUserData() {
        if(isDeleteUser){
            userMethods.deleteUser()
                    .then()
                    .statusCode(SC_ACCEPTED)
                    .and()
                    .assertThat().body("message", equalTo("User successfully removed"));
        }}
}
