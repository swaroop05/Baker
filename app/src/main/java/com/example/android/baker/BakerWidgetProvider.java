package com.example.android.baker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.baker.data.Baking;
import com.example.android.baker.data.QueryUtils;

import java.util.List;

import static com.example.android.baker.RecipeListFragment.getAllIngredientsDetails;

/**
 * Implementation of App Widget functionality.
 */
public class BakerWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = BakerWidgetProvider.class.getName();
    public static List<Baking> allBakingRecipes;
    private static int currentRecipeIndex;
    private static int currentService = 0;

    public static int getCurrentRecipeIndex() {
        return currentRecipeIndex;
    }

    public static void setCurrentRecipeIndex(int recipeIndex) {
        currentRecipeIndex = recipeIndex;
        Log.d(LOG_TAG, "Recipe index is now:- " + currentRecipeIndex);
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        if (currentService == Integer.MAX_VALUE) {
            currentService = 0;
        }
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baker_widget_provider);


        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.baker_widget_recipe_name, pendingIntent);

        //Intent to be set when 'Next' button is clicked
        Intent nextIntent = BakerWidgetService.getActionNextRecipeIntent(context);
        PendingIntent nextPendingIntent = PendingIntent.getService(context, ++currentService, nextIntent, 0);
        views.setOnClickPendingIntent(R.id.recipe_widget_next, nextPendingIntent);

        //Intent to be set when 'Previous' button is clicked
        Intent previousIntent = BakerWidgetService.getActionPreviousRecipeIntent(context);
        PendingIntent previousPendingIntent = PendingIntent.getService(context, ++currentService, previousIntent, 0);
        views.setOnClickPendingIntent(R.id.recipe_widget_previous, previousPendingIntent);


        if (allBakingRecipes == null) {
            new LoadBakingRecipesTask(views, appWidgetManager, appWidgetId).execute(context);
        } else {
            views.setTextViewText(R.id.baker_widget_recipe_name, allBakingRecipes.get(getCurrentRecipeIndex()).getBakingItemName());
            views.setTextViewText(R.id.baker_widget_recipe_ingredients, getAllIngredientsDetails(allBakingRecipes.get(getCurrentRecipeIndex())));
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllWidgets(Context context, AppWidgetManager widgetManager, int appWidgetIds[]) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, widgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Async task to load the baking recipes and update the Widget textview
     */
    public static class LoadBakingRecipesTask extends AsyncTask<Context, Void, List<Baking>> {
        private RemoteViews views;
        private int widgetID;
        private AppWidgetManager widgetManager;

        public LoadBakingRecipesTask(RemoteViews views, AppWidgetManager appWidgetManager,
                                     int appWidgetId) {
            this.views = views;
            this.widgetID = appWidgetId;
            this.widgetManager = appWidgetManager;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param contexts The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected List<Baking> doInBackground(Context... contexts) {
            Log.d(LOG_TAG, "LoadBakingRecipesTask Class -  doInBackground method is called now");
            return QueryUtils.fetchBakingInfo(contexts[0]);
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param bakingRecipesInfo The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(List<Baking> bakingRecipesInfo) {
            super.onPostExecute(bakingRecipesInfo);
            Log.d(LOG_TAG, "LoadBakingRecipesTask Class -  onPostExecute Method is called now");
            allBakingRecipes = bakingRecipesInfo;
            views.setTextViewText(R.id.baker_widget_recipe_name, bakingRecipesInfo.get(getCurrentRecipeIndex()).getBakingItemName());
            views.setTextViewText(R.id.baker_widget_recipe_ingredients, getAllIngredientsDetails(bakingRecipesInfo.get(getCurrentRecipeIndex())));
            widgetManager.updateAppWidget(widgetID, views);
        }
    }
}

