package com.example.android.baker;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by meets on 12/6/2018.
 */

public class IngredientsListWidgetService extends RemoteViewsService {
    private static final String LOG_TAG = IngredientsListWidgetService.class.getName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(LOG_TAG, "IngredientsListWidgetService Class is called now");
        return new IngredientsListWidgetProvider(this, intent);
    }
}
