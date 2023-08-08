package org.example.Order;

import lombok.Getter;
import lombok.Setter;

import static io.restassured.RestAssured.given;
@Getter
@Setter
public class Order {

    private String[] ingredients;

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }



}
