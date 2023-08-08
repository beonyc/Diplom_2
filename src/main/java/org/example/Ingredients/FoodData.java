package org.example.Ingredients;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@Getter
@Setter
@ToString
public class FoodData {
    private Boolean success;
    private List<FoodItem> data;

    //метод для десериализации каждого _id из JSON в список, для дальнейшей работы с ними
    public List<String> getAllIngredientsId() {
        Gson gson = new Gson();
        String json = given().baseUri("https://stellarburgers.nomoreparties.site/api/")
                .get("ingredients").asString();
        FoodData foodData = gson.fromJson(json, FoodData.class);
        ArrayList<String> listOfId = new ArrayList<>();
        for (FoodItem item : foodData.getData()) {
            listOfId.add(item.get_id());
        }
        return listOfId;
    }
}
