package com.example.android.baker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baker.data.BakingSteps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.baker.RecipeListFragment.KEY_INGREDIENTS_OF_SINGLE_RECIPE;
import static com.example.android.baker.RecipeListFragment.KEY_RECIPE_ID;
import static com.example.android.baker.RecipeListFragment.KEY_RECIPE_NAME;

/**
 * Created by meets on 10/2/2018.
 */

public class RecipeFragment extends Fragment {
    private static final String LOG_TAG = RecipeFragment.class.getName();

    @BindView(R.id.tv_ingredients_info)
    TextView mIngredientsInfoTextView;

    @BindView(R.id.rv_steps)
    RecyclerView mStepsRecyclerView;

    private static String mRecipeName;
    private static String mIngredientsDetails;

    private List<BakingSteps> mBakingSteps;
    private static List<BakingSteps> sBakingSteps;
    private static int mRecipeId;
    public static final String KEY_STEP_ID = "step_id";
    public static final String KEY_STEP_DESC = "step_desc";
    public static final String KEY_STEP_VIDEO_URL = "step_video_url";
    StepsContentAdapter.StepsItemClickListener mCallback;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            mRecipeName = savedInstanceState.getString(KEY_RECIPE_NAME);
            mIngredientsDetails = savedInstanceState.getString(KEY_INGREDIENTS_OF_SINGLE_RECIPE);
            mRecipeId = savedInstanceState.getInt(KEY_RECIPE_ID);

        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mRecipeName = bundle.getString(KEY_RECIPE_NAME) ;
                mIngredientsDetails = bundle.getString(KEY_INGREDIENTS_OF_SINGLE_RECIPE);
                mRecipeId = bundle.getInt(KEY_RECIPE_ID, 0);
            }

        }
        mBakingSteps = RecipeListFragment.getStepsDetailsOfRecipe(mRecipeId);
        sBakingSteps = mBakingSteps;

       // mIngredientsInfoTextView = rootView.findViewById(R.id.tv_ingredients_info);
        mIngredientsInfoTextView.setText(mIngredientsDetails);
       // mStepsRecyclerView = rootView.findViewById(R.id.rv_steps);
        updateStepsInUI(mBakingSteps);
        return rootView;
    }

    private void updateStepsInUI(List<BakingSteps> bakingStepsInfo) {
        Log.d(LOG_TAG, "updateUI Method is called now");
        StepsContentAdapter stepsContentAdapter = new StepsContentAdapter(bakingStepsInfo,mCallback );
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mStepsRecyclerView.setLayoutManager(layoutManager);
        mStepsRecyclerView.setNestedScrollingEnabled(false);
        mStepsRecyclerView.setAdapter(stepsContentAdapter);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (StepsContentAdapter.StepsItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

}
