package com.example.android.baker.data;

/**
 * Created by meets on 10/1/2018.
 */

public class BakingIngredients {
    private String quantity;
    private String measure;
    private String ingredient;

    public BakingIngredients(String quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
