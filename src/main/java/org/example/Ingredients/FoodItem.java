package org.example.Ingredients;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static io.restassured.RestAssured.given;
@Getter
@Setter
@ToString
public class FoodItem {
    private String _id;
    private String name;
    private String type;
    private int proteins;
    private int fat;
    private int carbohydrates;
    private int calories;
    private int price;
    private String image;
    private String image_mobile;
    private String image_large;
    private int __v;

}
