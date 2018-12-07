package com.example.android.baker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.baker.data.BakingIngredients;

import java.util.List;

/**
 * Created by meets on 12/6/2018.
 */

public class IngredientsListWidgetProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private static final String LOG_TAG = IngredientsListWidgetProvider.class.getName();
    private List<BakingIngredients> singleRecipeIngredientsList;
    private int currentRecipeIndex;

    public IngredientsListWidgetProvider(Context context, Intent intent) {
        this.context = context;
        Bundle bundle = intent.getBundleExtra(BakerWidgetProvider.RECIPE_BUNDLE);
        this.currentRecipeIndex = bundle.getInt(BakerWidgetProvider.RECIPE_INDEX);
        ;
        this.singleRecipeIngredientsList = BakerWidgetProvider.getIngredientsOfRecipe(currentRecipeIndex);
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.d(LOG_TAG, "onDataSetChanged method is called now");
        singleRecipeIngredientsList = BakerWidgetProvider.getIngredientsOfRecipe(BakerWidgetProvider.getCurrentRecipeIndex());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return singleRecipeIngredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(LOG_TAG, "getViewAt method is called now");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baker_widget_ingredient_item);
        BakingIngredients currentIngredient = singleRecipeIngredientsList.get(position);
        String ingredientName = currentIngredient.getIngredient();
        remoteViews.setTextViewText(R.id.recipe_widget_ingredient, ingredientName.substring(0, 1).toUpperCase() + ingredientName.substring(1));
        remoteViews.setTextViewText(R.id.recipe_widget_ingredient_measure_and_qty, currentIngredient.getQuantity() + " " + currentIngredient.getMeasure());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
