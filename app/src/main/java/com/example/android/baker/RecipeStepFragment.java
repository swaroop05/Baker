package com.example.android.baker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.baker.data.BakingSteps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.baker.DetailsActivity.KEY_STEP_DESC;
import static com.example.android.baker.DetailsActivity.KEY_STEP_ID;
import static com.example.android.baker.DetailsActivity.KEY_STEP_RECIPE_IMAGE;
import static com.example.android.baker.DetailsActivity.KEY_STEP_VIDEO_URL;
import static com.example.android.baker.RecipeListFragment.KEY_INGREDIENTS_OF_SINGLE_RECIPE;
import static com.example.android.baker.RecipeListFragment.KEY_RECIPE_ID;
import static com.example.android.baker.RecipeListFragment.KEY_RECIPE_NAME;

/**
 * Created by meets on 10/3/2018.
 */

public class RecipeStepFragment extends Fragment {
    private static final String LOG_TAG = RecipeStepFragment.class.getName();
    private String mStepDesc;
    private int mStepId;
    private int mRecipeId;
    private String mStepVideoUrl;
    private String mStepImageUrl;

    @Nullable
    @BindView(R.id.tv_steps_desc)
    TextView mStepDescriptionTextView;

    @Nullable
    @BindView(R.id.btn_previous_step_navigation)
    Button mPreviousStepBtn;

    @Nullable
    @BindView(R.id.btn_next_step_navigation)
    Button mNextStepBtn;

    @Nullable
    @BindView((R.id.recipe_steps_image))
    ImageView mStepImageView;

    /*//@BindView(R.id.tv_steps_title)
    TextView mStepsHeader;*/

    @BindView(R.id.video_view)
    SimpleExoPlayerView mPlayerView;

    private SimpleExoPlayer mExoPlayer;
    private List<BakingSteps> mBakingSteps;
    private long seekPosition = -1;
    private static final String SEEK_POSITION = "seekPosition";
    private boolean isTablet;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView;

        isTablet = getResources().getBoolean(R.bool.isTablet);
        if (savedInstanceState != null) {
            mStepDesc = savedInstanceState.getString(KEY_STEP_DESC);
            mStepVideoUrl = savedInstanceState.getString(KEY_STEP_VIDEO_URL);
            mRecipeId = savedInstanceState.getInt(KEY_RECIPE_ID);
            mStepId = savedInstanceState.getInt(KEY_STEP_ID);
            mStepImageUrl = savedInstanceState.getString(KEY_STEP_RECIPE_IMAGE);

        }else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                mStepDesc = bundle.getString(KEY_STEP_DESC) ;
                mStepVideoUrl = bundle.getString(KEY_STEP_VIDEO_URL);
                mStepImageUrl = bundle.getString(KEY_STEP_RECIPE_IMAGE);
                mStepId = bundle.getInt(KEY_STEP_ID, 0);
                mRecipeId = bundle.getInt(KEY_RECIPE_ID, 0);
            }
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(SEEK_POSITION)) {
            seekPosition = savedInstanceState.getLong(SEEK_POSITION);
        }

        if (isLandscape() && !isTablet){
            rootView = inflater.inflate(R.layout.fragment_recipe_step_land, container, false);
            ButterKnife.bind(this, rootView);
        } else {
            rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
            ButterKnife.bind(this, rootView);
           //mStepDescriptionTextView = rootView.findViewById(R.id.tv_steps_desc);
          // mPreviousStepBtn = rootView.findViewById(R.id.btn_previous_step_navigation);
         //  mNextStepBtn = rootView.findViewById(R.id.btn_next_step_navigation);
         //  mStepsHeader = rootView.findViewById(R.id.tv_steps_title);
            mPreviousStepBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStepId = mStepId - 1;
                    if ((DetailsActivity.getBakingStepsInfo(mStepId)) != null){
                        Bundle previousStepBundle =new Bundle();
                        previousStepBundle.putString(KEY_STEP_DESC,DetailsActivity.getBakingStepsInfo(mStepId).getBakingStepsDescription() );
                        previousStepBundle.putString(KEY_STEP_VIDEO_URL, DetailsActivity.getBakingStepsInfo(mStepId).getBakingStepsVideoUrl());
                        previousStepBundle.putString(KEY_STEP_RECIPE_IMAGE, DetailsActivity.getBakingStepsInfo(mStepId).getBakingStepsThumbnailUrl());
                        previousStepBundle.putInt(KEY_STEP_ID, mStepId);
                        previousStepBundle.putInt(KEY_RECIPE_ID,mRecipeId);
                        //Check if thie RecipeID can be deleted since this is not used anywhere

                        RecipeStepFragment fragment = RecipeStepFragment.newInstance(previousStepBundle);
                        seekPosition = -1;
                        replaceFragment(fragment);
                       /* mStepDesc = DetailsActivity.getBakingStepsInfo(mStepId).getBakingStepsDescription();
                        mStepVideoUrl = DetailsActivity.getBakingStepsInfo(mStepId).getBakingStepsVideoUrl();

                        updateUi ();*/
                    }else {
                        mStepId = mStepId + 1;
                        displayToastMessage("Reached First Step");
                    }
                }
            });

            mNextStepBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStepId = mStepId + 1;
                    if ((DetailsActivity.getBakingStepsInfo(mStepId)) != null){
                        Bundle nextStepBundle =new Bundle();
                        nextStepBundle.putString(KEY_STEP_DESC,DetailsActivity.getBakingStepsInfo(mStepId).getBakingStepsDescription() );
                        nextStepBundle.putString(KEY_STEP_VIDEO_URL, DetailsActivity.getBakingStepsInfo(mStepId).getBakingStepsVideoUrl());
                        nextStepBundle.putString(KEY_STEP_RECIPE_IMAGE, DetailsActivity.getBakingStepsInfo(mStepId).getBakingStepsThumbnailUrl());
                        nextStepBundle.putInt(KEY_STEP_ID, mStepId);
                        nextStepBundle.putInt(KEY_RECIPE_ID,mRecipeId);
                        //Check if thie RecipeID can be deleted since this is not used anywhere

                        RecipeStepFragment fragment = RecipeStepFragment.newInstance(nextStepBundle);
                        seekPosition = -1;
                        replaceFragment(fragment);


                       /* mStepDesc = DetailsActivity.getBakingStepsInfo(mStepId).getBakingStepsDescription();
                        mStepVideoUrl = DetailsActivity.getBakingStepsInfo(mStepId).getBakingStepsVideoUrl();
                        seekPosition = -1;
                        updateUi ();*/
                    }else {
                        mStepId = mStepId - 1;
                        displayToastMessage("Reached Last Step");
                    }
                }
            });
        }

      // mPlayerView = rootView.findViewById(R.id.video_view);
        updateUi ();
        return rootView;
    }

    private void replaceFragment(Fragment fragment) {

        if (getActivity() instanceof RecipeStepActivity) {
            ((RecipeStepActivity) getActivity()).replaceFragment(fragment, R.id.step_and_video_container_fragment, RecipeStepFragment.LOG_TAG, this);
        }else if (getActivity() instanceof DetailsActivity){
            ((DetailsActivity) getActivity()).replaceFragment(fragment, R.id.step_and_video_container_fragment, RecipeStepFragment.LOG_TAG, this);
        }
    }
    public static RecipeStepFragment newInstance(Bundle bundle) {
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setArguments(bundle);
        return recipeStepFragment;
    }

    public boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
    private void updateUi (){
        if (!isLandscape() || isTablet){
            mStepDescriptionTextView.setText(mStepDesc);
        }
        if (mExoPlayer != null){
            releasePlayer();
        }
        if (!mStepVideoUrl.isEmpty() ){
            if (!isLandscape()){
                mStepImageView.setVisibility(View.GONE);
            }

            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(mStepVideoUrl));
        }else if (!mStepImageUrl.isEmpty()){
            if (mStepImageUrl.contains(".mp4")){
                if (!isLandscape()){
                    mStepImageView.setVisibility(View.GONE);
                }
                mPlayerView.setVisibility(View.VISIBLE);
                initializePlayer(Uri.parse(mStepImageUrl));
            }else {
                if (!isLandscape()){
                    mPlayerView.setVisibility(View.GONE);
                    mStepImageView.setVisibility(View.VISIBLE);
                    Picasso.with(getContext())
                            .load(mStepImageUrl)
                            .into(mStepImageView);
                }

            }


        } else{
            mPlayerView.setVisibility(View.GONE);
            mStepImageView.setVisibility(View.GONE);
        }
    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(true);
            if (seekPosition != -1) {
                mExoPlayer.seekTo(seekPosition);
            }
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "Baking");
            /*MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);*/
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultHttpDataSourceFactory("ua"),
                    new DefaultExtractorsFactory(), null, null);

            mExoPlayer.prepare(mediaSource, true, false);

        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isLandscape() && !isTablet){
            hideSystemUi();
            if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
                initializePlayer(Uri.parse(mStepVideoUrl));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            seekPosition = mExoPlayer.getCurrentPosition();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            seekPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_STEP_DESC, mStepDesc);
        outState.putInt(KEY_STEP_ID, mStepId);
        outState.putString(KEY_STEP_VIDEO_URL, mStepVideoUrl);
        outState.putString(KEY_STEP_RECIPE_IMAGE, mStepImageUrl);
        outState.putInt(KEY_RECIPE_ID, mRecipeId);
        if (mExoPlayer != null) {
            outState.putLong(SEEK_POSITION, seekPosition);
        }

    }

    /**
     * Method to show Toast messages
     *
     * @param message
     */
    public void displayToastMessage(String message) {
        Log.d(LOG_TAG, "displayToastMessage  Method is called now");
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

}
