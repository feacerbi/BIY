package br.com.felipeacerbi.biy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.adapters.listeners.IRecipeStepClickListener;
import br.com.felipeacerbi.biy.models.Step;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter extends RecyclerView.Adapter {

    private List<Step> mSteps;
    private IRecipeStepClickListener mListener;

    private int mSelectedItem = 0;

    public RecipeStepsAdapter(List<Step> mSteps, IRecipeStepClickListener mListener) {
        this.mSteps = mSteps;
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_list_item, parent, false);
        return new RecipeStepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Step step = mSteps.get(position);
        final RecipeStepViewHolder recipeStepViewHolder = (RecipeStepViewHolder) holder;

        recipeStepViewHolder.description.setText(step.getShortDescription());
        recipeStepViewHolder.id.setText(String.valueOf(step.getId() + 1));

        selectItem(recipeStepViewHolder, position == mSelectedItem);

        recipeStepViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRecipeStepClicked(step);
                mSelectedItem = recipeStepViewHolder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    public void setItems(List<Step> items) {
        mSteps = items;
        notifyDataSetChanged();
    }

    private void selectItem(RecipeStepViewHolder recipeStepViewHolder, boolean selected) {
        if(selected) {
            recipeStepViewHolder.itemView.setBackgroundColor(mListener.getContext().getResources().getColor(R.color.colorAccent, mListener.getContext().getTheme()));
            recipeStepViewHolder.id.setTextColor(mListener.getContext().getResources().getColor(R.color.background_default, mListener.getContext().getTheme()));
            recipeStepViewHolder.description.setTextColor(mListener.getContext().getResources().getColor(R.color.background_default, mListener.getContext().getTheme()));
        } else {
            recipeStepViewHolder.itemView.setBackgroundColor(mListener.getContext().getResources().getColor(R.color.background_default, mListener.getContext().getTheme()));
            recipeStepViewHolder.id.setTextColor(mListener.getContext().getResources().getColor(R.color.colorAccent, mListener.getContext().getTheme()));
            recipeStepViewHolder.description.setTextColor(mListener.getContext().getResources().getColor(R.color.colorAccent, mListener.getContext().getTheme()));
        }
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_step_short_description) TextView description;
        @BindView(R.id.tv_step_id) TextView id;

        RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
