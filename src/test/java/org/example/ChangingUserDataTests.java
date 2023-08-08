package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.User.User;
import org.example.User.UserMethods;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class ChangingUserDataTests {
    private UserMethods userMethods;

    //Переменная которая говорит, нужно ли удалить пользователя true-да, false-нет
    private boolean isDeleteUser = true;

    @Before
    public void setUp() {
        userMethods = new UserMethods();
    }

    @Test
    @DisplayName("Изменение имени пользователя")
    @Description("Проверка что после изменения имени пользователя получим код-200 и success: true")
    public void changeUserInformationNameTest() {

        String newName = "practicum";
        User userData=new User();
        userData.setName(newName);

        userMethods.createUser();
        userMethods.changeUserInformation(userMethods.getUserAccessToken(), userData)
                .then().statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true));
        String oldName = userMethods.getUserDataResponse().then().extract().path("user.name");
        //Проверка, что имена разные
        Assert.assertNotEquals(newName, oldName);
    }
    @Test
    @DisplayName("Изменение email пользователя")
    @Description("Проверка что после изменения email пользователя получим код-200 и success: true")
    public void changeUserInformationEmailTest() {
        String newEmail = "practicumFacaa@yandex.ru";
        User userData=new User();
        userData.setEmail(newEmail);

        userMethods.createUser();
        userMethods.changeUserInformation(userMethods.getUserAccessToken(), userData)
                .then().statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true));
        String oldEmail = userMethods.getUserDataResponse().then().extract().path("user.email");
        //Проверка, что эмайлы разные
        Assert.assertNotEquals(newEmail, oldEmail);
        //обновляем email, для того чтобы удалить пользователя в методе с аннотацией @After
        userMethods.setEmailForUser(newEmail);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Проверка что после попытки изменения данных пользователя без авторизации получим код-401 и message:You should be authorised")
    public void changeUserInformationWithoutAuthTest() {
        isDeleteUser =false;
        userMethods.changeUserInformationWithoutAuthorization()
                .then().statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
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
