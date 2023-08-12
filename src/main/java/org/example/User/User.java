package org.example.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class User {
    private String email;
    private String password;
    private String name;

    public User() {
        this.email = "someEmailForTesting666@yandex.ru";
        this.password = "password";
        this.name = "Erik";
    }

    public User getUserWithoutEmail() {
        return new User(null, getPassword(), getName());
    }
}
