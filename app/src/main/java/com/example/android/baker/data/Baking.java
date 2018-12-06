package com.example.android.baker.data;

import java.util.ArrayList;

/**
 * Created by meets on 10/1/2018.
 */

public class Baking {
    private String bakingItemId;
    private String bakingItemName;
    private ArrayList<BakingIngredients> bakingIngredientsArrayList;
    private ArrayList<BakingSteps> bakingStepsArrayList;
    private String servings;
    private String image;

    /**
     * Constructor to initialize the variables
     *
     * @param bakingItemId
     * @param bakingItemName
     * @param bakingIngredientsArrayList
     * @param bakingStepsArrayList
     * @param servings
     * @param image
     */
    public Baking(String bakingItemId, String bakingItemName, ArrayList<BakingIngredients> bakingIngredientsArrayList, ArrayList<BakingSteps> bakingStepsArrayList, String servings, String image) {
        this.bakingItemId = bakingItemId;
        this.bakingItemName = bakingItemName;
        this.bakingIngredientsArrayList = bakingIngredientsArrayList;
        this.bakingStepsArrayList = bakingStepsArrayList;
        this.servings = servings;
        this.image = image;
    }

    public String getBakingItemId() {
        return bakingItemId;
    }

    public String getBakingItemName() {
        return bakingItemName;
    }

    public ArrayList<BakingIngredients> getBakingIngredientsArrayList() {
        return bakingIngredientsArrayList;
    }

    public ArrayList<BakingSteps> getBakingStepsArrayList() {
        return bakingStepsArrayList;
    }

    public String getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
