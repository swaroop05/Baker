package com.example.android.baker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.baker.data.BakingSteps;

import java.util.List;

import static com.example.android.baker.RecipeListFragment.KEY_INGREDIENTS_OF_SINGLE_RECIPE;
import static com.example.android.baker.RecipeListFragment.KEY_RECIPE_ID;
import static com.example.android.baker.RecipeListFragment.KEY_RECIPE_NAME;

/**
 * Created by meets on 10/2/2018.
 */

public class DetailsActivity extends AppCompatActivity implements StepsContentAdapter.StepsItemClickListener {

    private static final String LOG_TAG = DetailsActivity.class.getName();
    private static String mRecipeName;
    private static String mIngredientsDetails;
    private static int mRecipeId;
    private List<BakingSteps> mBakingSteps;
    private static List<BakingSteps> sBakingSteps;
    public static final String KEY_STEP_ID = "step_id";
    public static final String KEY_STEP_DESC = "step_desc";
    public static final String KEY_STEP_VIDEO_URL = "step_video_url";
    public static final String KEY_STEP_RECIPE_IMAGE = "recipe_image_url";
    private boolean isTablet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate is called now");
        isTablet = getResources().getBoolean(R.bool.isTablet);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_RECIPE_NAME)) {
            mRecipeName = intent.getStringExtra(KEY_RECIPE_NAME);
        }
        if (intent.hasExtra(KEY_INGREDIENTS_OF_SINGLE_RECIPE)) {
            mIngredientsDetails = intent.getStringExtra(KEY_INGREDIENTS_OF_SINGLE_RECIPE);
        }
        if (intent.hasExtra(KEY_RECIPE_ID)) {
            mRecipeId = intent.getIntExtra(KEY_RECIPE_ID, 0);
        }
        mBakingSteps = RecipeListFragment.getStepsDetailsOfRecipe(mRecipeId);
        sBakingSteps = mBakingSteps;
        setTitle(mRecipeName);
        Bundle recipeFragmentBundle = new Bundle();
        recipeFragmentBundle.putString(KEY_RECIPE_NAME, mRecipeName);
        recipeFragmentBundle.putString(KEY_INGREDIENTS_OF_SINGLE_RECIPE, mIngredientsDetails);
        recipeFragmentBundle.putInt(KEY_RECIPE_ID,mRecipeId);
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(recipeFragmentBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_fragment_container, recipeFragment)
                .commit();

        if (isTablet){
            int stepId = 0;
            BakingSteps bakingStep = mBakingSteps.get(stepId);
            String bakingStepDescription = bakingStep.getBakingStepsDescription();
            String bakingStepVideoUrl = bakingStep.getBakingStepsVideoUrl();
            Bundle recipeStepFragmentBundle = new Bundle();
            recipeStepFragmentBundle.putString(KEY_STEP_DESC,bakingStepDescription );
            recipeStepFragmentBundle.putString(KEY_STEP_VIDEO_URL, bakingStepVideoUrl);
            recipeStepFragmentBundle.putInt(KEY_STEP_ID, stepId);
            recipeStepFragmentBundle.putInt(KEY_RECIPE_ID,mRecipeId);
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setArguments(recipeStepFragmentBundle);
            recipeStepFragment.setRetainInstance(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_and_video_container_fragment, recipeStepFragment)
                    .commit();
        }

    }

    @Override
    public void onStepsItemClick(int clickedItemIndex) {
        BakingSteps bakingStep = mBakingSteps.get(clickedItemIndex);
        String bakingStepDescription = bakingStep.getBakingStepsDescription();
        String bakingStepVideoUrl = bakingStep.getBakingStepsVideoUrl();
        String bakingImageUrl = bakingStep.getBakingStepsThumbnailUrl();
        int stepId = clickedItemIndex;
        if (isTablet){
            Bundle recipeStepFragmentBundle = new Bundle();
            recipeStepFragmentBundle.putString(KEY_STEP_DESC,bakingStepDescription );
            recipeStepFragmentBundle.putString(KEY_STEP_VIDEO_URL, bakingStepVideoUrl);
            recipeStepFragmentBundle.putInt(KEY_STEP_ID, stepId);
            recipeStepFragmentBundle.putInt(KEY_RECIPE_ID,mRecipeId);
            recipeStepFragmentBundle.putString(KEY_STEP_RECIPE_IMAGE,bakingImageUrl);
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setArguments(recipeStepFragmentBundle);
            recipeStepFragment.setRetainInstance(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_and_video_container_fragment, recipeStepFragment)
                    .commit();
        }else {
            Intent viewRecipeStepActivityIntent = new Intent(this, RecipeStepActivity.class);
            viewRecipeStepActivityIntent.putExtra(KEY_STEP_DESC, bakingStepDescription);
            viewRecipeStepActivityIntent.putExtra(KEY_STEP_ID, stepId);
            viewRecipeStepActivityIntent.putExtra(KEY_RECIPE_ID, mRecipeId);
            viewRecipeStepActivityIntent.putExtra(KEY_STEP_VIDEO_URL, bakingStepVideoUrl);
            viewRecipeStepActivityIntent.putExtra(KEY_STEP_RECIPE_IMAGE, bakingImageUrl);
            startActivity(viewRecipeStepActivityIntent);
        }



    }

    public static BakingSteps getBakingStepsInfo(int stepId) {
        BakingSteps bakingStep;
        bakingStep = null;
        try {
            bakingStep = sBakingSteps.get(stepId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bakingStep;
    }
}
