package org.example.Order;

import lombok.Data;

@Data
public class Order {

    private String[] ingredients;

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }



}
