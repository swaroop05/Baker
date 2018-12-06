package com.example.android.baker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import static com.example.android.baker.DetailsActivity.KEY_STEP_DESC;
import static com.example.android.baker.DetailsActivity.KEY_STEP_ID;
import static com.example.android.baker.DetailsActivity.KEY_STEP_RECIPE_IMAGE;
import static com.example.android.baker.DetailsActivity.KEY_STEP_VIDEO_URL;

/**
 * Created by meets on 10/3/2018.
 */

public class RecipeStepActivity extends AppCompatActivity {
    public static final String KEY_RECIPE_ID = "recipe_id";
    private String mStepDesc;
    private int mStepId;
    private int mRecipeId;
    private String mStepVideoUrl;
    private String mStepImageUrl;

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
            if (intent.hasExtra(KEY_STEP_RECIPE_IMAGE)) {
                mStepImageUrl = intent.getStringExtra(KEY_STEP_RECIPE_IMAGE);
            }

            Bundle recipeStepFragmentBundle = new Bundle();
            recipeStepFragmentBundle.putString(KEY_STEP_DESC, mStepDesc);
            recipeStepFragmentBundle.putString(KEY_STEP_VIDEO_URL, mStepVideoUrl);
            recipeStepFragmentBundle.putString(KEY_STEP_RECIPE_IMAGE, mStepImageUrl);
            recipeStepFragmentBundle.putInt(KEY_STEP_ID, mStepId);
            recipeStepFragmentBundle.putInt(KEY_RECIPE_ID, mRecipeId);
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setArguments(recipeStepFragmentBundle);
            recipeStepFragment.setRetainInstance(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_and_video_container_fragment, recipeStepFragment)
                    .commit();
        }
    }

    /**
     * replaces the fragment in mobile mode
     *
     * @param newFragment
     * @param containerId
     * @param TAG
     * @param oldFragment
     */
    public void replaceFragment(Fragment newFragment, @IdRes int containerId, String TAG, Fragment oldFragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(containerId, newFragment, TAG)
                .commit();
    }
}
