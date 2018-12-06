package com.example.android.baker;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.baker.data.BakingSteps;

import java.util.List;

/**
 * Created by meets on 10/2/2018.
 */

public class StepsContentAdapter extends RecyclerView.Adapter<StepsContentAdapter.StepsContentViewsHolder> {

    private static final String TAG = StepsContentAdapter.class.getSimpleName();
    final private StepsContentAdapter.StepsItemClickListener mStepsContentItemClickListener;
    private CardView stepsCardView;
    private TextView stepsContentTextView;
    private List<BakingSteps> mBakingStepsInfos;

    public StepsContentAdapter(List<BakingSteps> bakingSteps, StepsItemClickListener mStepsContentItemClickListener) {
        this.mStepsContentItemClickListener = mStepsContentItemClickListener;
        mBakingStepsInfos = bakingSteps;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public StepsContentViewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForStepsList = R.layout.steps_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForStepsList, parent, shouldAttachToParentImmediately);
        StepsContentAdapter.StepsContentViewsHolder stepsContentViewsHolder = new StepsContentAdapter.StepsContentViewsHolder(view);
        return stepsContentViewsHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(StepsContentViewsHolder holder, int position) {
        BakingSteps bakingStepsInfos = mBakingStepsInfos.get(position);
        holder.bind(bakingStepsInfos);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mBakingStepsInfos.size();
    }

    /**
     * Interface for handling Steps Item click
     */
    public interface StepsItemClickListener {
        void onStepsItemClick(int clickedItemIndex);
    }


    class StepsContentViewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public StepsContentViewsHolder(View itemView) {
            super(itemView);
            stepsCardView = itemView.findViewById(R.id.cv_steps);
            stepsContentTextView = itemView.findViewById(R.id.tv_steps_content);
            stepsContentTextView.setOnClickListener(this);
        }

        void bind(BakingSteps bakingStepsInfo) {
            stepsContentTextView.setText(bakingStepsInfo.getBakingStepsShortDescription());
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mStepsContentItemClickListener.onStepsItemClick(clickedPosition);
        }
    }
}
