package com.example.android.baker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static com.example.android.baker.DetailsActivity.KEY_STEP_DESC;
import static com.example.android.baker.DetailsActivity.KEY_STEP_ID;
import static com.example.android.baker.DetailsActivity.KEY_STEP_VIDEO_URL;
import static com.example.android.baker.RecipeListFragment.KEY_RECIPE_ID;

/**
 * Created by meets on 10/3/2018.
 */

public class RecipeStepActivity extends AppCompatActivity {
    private static final String LOG_TAG = RecipeStepActivity.class.getName();
    private String mStepDesc;
    private int mStepId;
    private int mRecipeId;
    private String mStepVideoUrl;
    public static final String KEY_RECIPE_ID ="recipe_id";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        if (savedInstanceState == null) {
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_STEP_DESC)) {
            mStepDesc = intent.getStringExtra(KEY_STEP_DESC);
        }
        if (intent.hasExtra(KEY_STEP_ID)) {
            mStepId = intent.getIntExtra(KEY_STEP_ID, 0);
        }
        if (intent.hasExtra(KEY_RECIPE_ID)) {
            mRecipeId = intent.getIntExtra(KEY_RECIPE_ID, 0);
        }
        if (intent.hasExtra(KEY_STEP_VIDEO_URL)) {
            mStepVideoUrl = intent.getStringExtra(KEY_STEP_VIDEO_URL);
        }
        Bundle recipeStepFragmentBundle = new Bundle();
        recipeStepFragmentBundle.putString(KEY_STEP_DESC,mStepDesc );
        recipeStepFragmentBundle.putString(KEY_STEP_VIDEO_URL, mStepVideoUrl);
        recipeStepFragmentBundle.putInt(KEY_STEP_ID, mStepId);
        recipeStepFragmentBundle.putInt(KEY_RECIPE_ID,mRecipeId);
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setArguments(recipeStepFragmentBundle);
        recipeStepFragment.setRetainInstance(false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_and_video_container_fragment, recipeStepFragment)
                .commit();

        }
    }



}
