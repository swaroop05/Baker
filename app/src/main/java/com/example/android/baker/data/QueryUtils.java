package com.example.android.baker.data;

import android.content.Context;
import android.util.Log;

import com.example.android.baker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by meets on 10/1/2018.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();
    private static final String KEY_RECIPE_ID = "id";
    private static final String KEY_RECIPE_NAME = "name";
    private static final String KEY_RECIPE_INGREDIENTS = "ingredients";
    private static final String KEY_RECIPE_INGREDIENT_QTY = "quantity";
    private static final String KEY_RECIPE_INGREDIENT_MEASURE = "measure";
    private static final String KEY_RECIPE_INGREDIENT = "ingredient";
    private static final String KEY_RECIPE_STEPS = "steps";
    private static final String KEY_RECIPE_STEP_ID = "id";
    private static final String KEY_RECIPE_STEP_SHORT_DESC = "shortDescription";
    private static final String KEY_RECIPE_STEP_DESCRIPTION = "description";
    private static final String KEY_RECIPE_STEP_VIDEO_URL = "videoURL";
    private static final String KEY_RECIPE_STEP_THUMBNAIL_URL = "thumbnailURL";
    private static final String KEY_RECIPE_SERVINGS = "servings";
    private static final String KEY_RECIPE_IMAGE_URL = "image";


    private static String readBakingJson(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.baking);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer.toString();
    }


    private static ArrayList<Baking> extractBakingInfo(String jsonResponse) {
        // Create an empty ArrayList that we can start adding bakingInfo to
        ArrayList<Baking> bakingArrayList = new ArrayList<>();
        try {
            if (jsonResponse != null) {
                JSONArray resultsArray = new JSONArray(jsonResponse);
                if (resultsArray != null) {
                    int resultsArrayCount = resultsArray.length();
                    for (int i = 0; i < resultsArrayCount; i++) {
                        JSONObject individualBakingItem = resultsArray.getJSONObject(i);
                        String recipeId = individualBakingItem.optString(KEY_RECIPE_ID);
                        String recipeName = individualBakingItem.optString(KEY_RECIPE_NAME);
                        JSONArray ingredientsArray = individualBakingItem.optJSONArray(KEY_RECIPE_INGREDIENTS);
                        ArrayList<BakingIngredients> ingredientsArrayList = new ArrayList<>();
                        if (ingredientsArray != null) {
                            int ingredientsArrayCount = ingredientsArray.length();
                            for (int j = 0; j < ingredientsArrayCount; j++) {
                                JSONObject individualIngredientDetails = ingredientsArray.getJSONObject(j);
                                String ingredientQty = individualIngredientDetails.optString(KEY_RECIPE_INGREDIENT_QTY);
                                String ingredientMeasure = individualIngredientDetails.optString(KEY_RECIPE_INGREDIENT_MEASURE);
                                String ingredient = individualIngredientDetails.optString(KEY_RECIPE_INGREDIENT);
                                ingredientsArrayList.add(new BakingIngredients(ingredientQty, ingredientMeasure, ingredient));
                            }
                        }

                        JSONArray stepsArray = individualBakingItem.optJSONArray(KEY_RECIPE_STEPS);
                        ArrayList<BakingSteps> stepsArrayList = new ArrayList<>();
                        if (stepsArray != null) {
                            int stepsArrayCount = stepsArray.length();
                            for (int k = 0; k < stepsArrayCount; k++) {
                                JSONObject individualStepDetails = stepsArray.getJSONObject(k);
                                String stepsId = individualStepDetails.optString(KEY_RECIPE_STEP_ID);
                                String stepShortDesc = individualStepDetails.optString(KEY_RECIPE_STEP_SHORT_DESC);
                                String stepDescription = individualStepDetails.optString(KEY_RECIPE_STEP_DESCRIPTION);
                                String stepVideoUrl = individualStepDetails.optString(KEY_RECIPE_STEP_VIDEO_URL);
                                String stepThumNailUrl = individualStepDetails.optString(KEY_RECIPE_STEP_THUMBNAIL_URL);
                                stepsArrayList.add(new BakingSteps(stepsId, stepShortDesc, stepDescription, stepVideoUrl, stepThumNailUrl));
                            }
                        }
                        String recipeServings = individualBakingItem.optString(KEY_RECIPE_SERVINGS);
                        String recipeImageUrl = individualBakingItem.optString(KEY_RECIPE_IMAGE_URL);

                        bakingArrayList.add(new Baking(recipeId, recipeName, ingredientsArrayList, stepsArrayList, recipeServings, recipeImageUrl));
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the Baking JSON results", e);
        }
        return bakingArrayList;
    }


    public static ArrayList<Baking> fetchBakingInfo(Context context) {

        String jsonResponse = "";
        try {
            jsonResponse = readBakingJson(context);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem making HttpRequest", e);
        }
        return extractBakingInfo(jsonResponse);
    }
}
