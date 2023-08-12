package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.User.UserMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTests {
    private UserMethods userMethods;
    //Переменная которая говорит, нужно ли удалить пользователя true-да, false-нет
    private boolean isDeleteUser =true;

    @Before
    public void setUp() {
        userMethods = new UserMethods();
    }

    @Test
    @DisplayName("Создание нового пользователя")
    @Description("Проверка что при создании нового пользователя получается: код-200 и success = true")
    public void createUserTest() {
        userMethods.createUser().then()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Повторное создание существующего пользователя")
    @Description("Проверка что при попытке создания существующего пользователя получается: код-403 и message:User already exists")
    public void createExistingUserTest() {
        userMethods.createUser();
        userMethods.createUser().then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("message", equalTo("User already exists"));
    }
    @Test
    @DisplayName("Создание пользователя без обязательного поля email")
    @Description("Проверка что при попытке создания пользователя без email получается: код-403 и message: Email, password and name are required fields")
    public void createUserWithoutEmailTest() {
        isDeleteUser =false;
        userMethods.createUserWithoutEmail().then()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @After
    @DisplayName("Удаление созданного пользователя")
    @Description("Проверка что после удаления мы получим код 202 и сообщение -User successfully removed ")
    public void deleteUserDataTest() {
        if(isDeleteUser){
            userMethods.deleteUser()
                    .then()
                    .statusCode(SC_ACCEPTED)
                    .and()
                    .assertThat().body("message", equalTo("User successfully removed"));
        }


    }

}