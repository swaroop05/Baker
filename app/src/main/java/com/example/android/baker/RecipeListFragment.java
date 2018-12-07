package com.example.android.baker;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.baker.data.Baking;
import com.example.android.baker.data.BakingIngredients;
import com.example.android.baker.data.BakingSteps;
import com.example.android.baker.data.QueryUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by meets on 10/1/2018.
 */

public class RecipeListFragment extends Fragment implements BakingRecipeNamesAdapter.RecipeNameItemClickListener {
    public static final String KEY_RECIPE_NAME = "recipeName";
    public static final String KEY_INGREDIENTS_OF_SINGLE_RECIPE = "singleRecipeIngredients";
    public static final String KEY_RECIPE_ID = "recipe_id";
    private static final String LOG_TAG = RecipeListFragment.class.getName();
    private static Parcelable STATE = null;
    private static List<Baking> BAKING_INFO_MASTER;
    @BindView(R.id.empty_view)
    TextView mEmptyStateTextView;
    @BindView(R.id.rv_recipe_names)
    RecyclerView mBakingRecipeNamesRecyclerView;
    @BindView(R.id.no_internet)
    TextView noInternetTextView;
    @BindView(R.id.loading_spinner)
    ProgressBar mProgressBar;
    private List<Baking> mBakingInfos;
    private boolean isTablet;

    /**
     * static method to get all ingredients of a given recipe and return as a single text
     *
     * @param singleBakingRecipeInfo
     * @return
     */
    public static String getAllIngredientsDetails(Baking singleBakingRecipeInfo) {
        String allIngredientsDetails = "";
        List<BakingIngredients> bakingIngredientsArrayList = singleBakingRecipeInfo.getBakingIngredientsArrayList();
        for (BakingIngredients bakingIngredient : bakingIngredientsArrayList) {
            String quantity = bakingIngredient.getQuantity();
            String measure = bakingIngredient.getMeasure();
            String ingredient = bakingIngredient.getIngredient();
            allIngredientsDetails = allIngredientsDetails + "- " + quantity + " " + measure + " of " + ingredient + "\n\n";

        }
        return allIngredientsDetails;
    }

    public static ArrayList<BakingIngredients> getIngredientDetailsAsList(Baking singleBakingRecipeInfo){
        return singleBakingRecipeInfo.getBakingIngredientsArrayList();
    }

    /**
     * static method to return Step details of given recipe based on recipe id.
     *
     * @param recipeId
     * @return
     */
    public static List<BakingSteps> getStepsDetailsOfRecipe(int recipeId) {
        List<BakingSteps> bakingStepsList = BAKING_INFO_MASTER.get(recipeId).getBakingStepsArrayList();
        return bakingStepsList;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);
        mProgressBar.setVisibility(View.GONE);
        isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isNetworkConnected()) {
            mProgressBar.setVisibility(View.VISIBLE);
            new LoadBakingItemsTask().execute(getContext());
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.GONE);
            noInternetTextView.setVisibility(View.VISIBLE);
            noInternetTextView.setText(R.string.no_internet);
        }
        return rootView;
    }

    @Override
    public void onRecipeNameItemClick(int clickedItemIndex) {
        Baking singleRecipeDetails = mBakingInfos.get(clickedItemIndex);
        String bakingRecipeName = singleRecipeDetails.getBakingItemName();
        String recipeId = singleRecipeDetails.getBakingItemId();
        String ingredientsDetails = getAllIngredientsDetails(singleRecipeDetails);
        List<BakingSteps> bakingStepsArrayList = singleRecipeDetails.getBakingStepsArrayList();
        Intent detailsActivityIntent = new Intent(getContext(), DetailsActivity.class);
        detailsActivityIntent.putExtra(KEY_RECIPE_NAME, bakingRecipeName);
        detailsActivityIntent.putExtra(KEY_INGREDIENTS_OF_SINGLE_RECIPE, ingredientsDetails);
        detailsActivityIntent.putExtra(KEY_RECIPE_ID, clickedItemIndex);
        startActivity(detailsActivityIntent);
    }

    /**
     * updates the UI with RecipeName and Servings details via adapter
     *
     * @param bakingRecipesInfo
     */
    private void updateUI(List<Baking> bakingRecipesInfo) {
        Log.d(LOG_TAG, "updateUI Method is called now");
        BakingRecipeNamesAdapter bakingRecipeNamesAdapter = new BakingRecipeNamesAdapter(bakingRecipesInfo, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        if (isTablet) {
            mBakingRecipeNamesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else {
            mBakingRecipeNamesRecyclerView.setLayoutManager(layoutManager);
        }
        mBakingRecipeNamesRecyclerView.setAdapter(bakingRecipeNamesAdapter);

    }

    /**
     * checks the network connection
     *
     * @return boolean value
     */
    private boolean isNetworkConnected() {
        Log.d(LOG_TAG, "isNetworkConnected Method is called now");
        boolean isConnected;
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /**
     * Async task to load the Baking Items from Json and update the UI
     */
    public class LoadBakingItemsTask extends AsyncTask<Context, Void, List<Baking>> {

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
            Log.d(LOG_TAG, "LoadBakingItemsTask Class -  doInBackground method is called now");
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
            Log.d(LOG_TAG, "LoadBakingItemsTask Class -  onPostExecute Method is called now");
            mBakingInfos = bakingRecipesInfo;
            BAKING_INFO_MASTER = bakingRecipesInfo;
            mProgressBar.setVisibility(View.GONE);
            if (bakingRecipesInfo == null) {
                updateUI(new ArrayList<Baking>());
            } else if (bakingRecipesInfo.size() == 0) {
                if (isNetworkConnected()) {
                    noInternetTextView.setVisibility(View.GONE);
                    updateUI(new ArrayList<Baking>());
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText(R.string.no_baking_info_found);
                } else {
                    updateUI(new ArrayList<Baking>());
                    mEmptyStateTextView.setVisibility(View.GONE);
                    noInternetTextView.setVisibility(View.VISIBLE);
                    noInternetTextView.setText(R.string.no_internet);
                }
            } else {
                if (isNetworkConnected()) {
                    noInternetTextView.setVisibility(View.GONE);
                    mEmptyStateTextView.setVisibility(View.GONE);
                    updateUI(bakingRecipesInfo);
                } else {
                    updateUI(new ArrayList<Baking>());
                    mEmptyStateTextView.setVisibility(View.GONE);
                    noInternetTextView.setVisibility(View.VISIBLE);
                    noInternetTextView.setText(R.string.no_internet);
                }
            }

        }
    }
}
