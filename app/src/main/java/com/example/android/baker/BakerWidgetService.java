package com.example.android.baker;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.baker.data.Baking;
import com.example.android.baker.data.QueryUtils;

import java.util.List;

/**
 * Created by meets on 12/1/2018.
 */

public class BakerWidgetService extends IntentService {
    private static final String LOG_TAG = BakerWidgetService.class.getName();
    private static final String ACTION_ON_WIDGET_LAUNCH = "com.example.android.baker.action.widget.launch";
    private static final String ACTION_ON_WIDGET_NEXT = "com.example.android.baker.action.widget.next";
    private static final String ACTION_ON_WIDGET_PREVIOUS = "com.example.android.baker.action.widget.previous";

    public static List<Baking> mBakingInfos;
    public static Context mContext;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public BakerWidgetService() {
        super("BakerWidgetService");
    }

    public static Intent getActionNextRecipeIntent(Context context) {
        Intent intent = new Intent(context, BakerWidgetService.class);
        intent.setAction(ACTION_ON_WIDGET_NEXT);
        return intent;
    }


    public static Intent getActionPreviousRecipeIntent(Context context) {
        Intent intent = new Intent(context, BakerWidgetService.class);
        intent.setAction(ACTION_ON_WIDGET_PREVIOUS);
        return intent;
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ON_WIDGET_LAUNCH.equals(action)) {
                //
            }else if (ACTION_ON_WIDGET_NEXT.equals(action)){
                int currentRecipeIndex = BakerWidgetProvider.getCurrentRecipeIndex();
                if(currentRecipeIndex <= 2){
                    BakerWidgetProvider.setCurrentRecipeIndex(currentRecipeIndex+1);
                }else {
                    BakerWidgetProvider.setCurrentRecipeIndex(0);
                }
                handleActionTraversal();
            }else if (ACTION_ON_WIDGET_PREVIOUS.equals(action)){
                int currentRecipeIndex = BakerWidgetProvider.getCurrentRecipeIndex();
                if(currentRecipeIndex == 0){
                    BakerWidgetProvider.setCurrentRecipeIndex(3);
                }else {
                    BakerWidgetProvider.setCurrentRecipeIndex(currentRecipeIndex-1);
                }
                handleActionTraversal();
            }
        }
    }

    private void handleActionTraversal() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakerWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh

        BakerWidgetProvider.updateAllWidgets(this, appWidgetManager, appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget_holder);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.baker_widget_recipe_name);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.baker_widget_recipe_ingredients);
    }



}
