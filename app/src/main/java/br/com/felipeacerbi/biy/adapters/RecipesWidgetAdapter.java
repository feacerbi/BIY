package br.com.felipeacerbi.biy.adapters;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.adapters.listeners.IRecipeClickListener;
import br.com.felipeacerbi.biy.models.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesWidgetAdapter extends RecyclerView.Adapter {

    private List<Recipe> mRecipes;
    private IRecipeClickListener mListener;

    public RecipesWidgetAdapter(List<Recipe> mRecipes, IRecipeClickListener mListener) {
        this.mRecipes = mRecipes;
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_widget_list_item, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Recipe recipe = mRecipes.get(position);
        final RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;

        recipeViewHolder.name.setText(recipe.getName());

        Pair<String, Integer> difficulty = Recipe.getRecipeDifficulty(mListener.getContext(), recipe);
        recipeViewHolder.difficulty.setText(difficulty.first);
        recipeViewHolder.difficulty.setTextColor(difficulty.second);

        recipeViewHolder.ingredientsCount.setText(String.format(
                Locale.getDefault(),
                mListener.getContext().getResources().getString(R.string.card_ingredients),
                recipe.getIngredients().size()));
        recipeViewHolder.servings.setText(String.format(
                Locale.getDefault(),
                mListener.getContext().getString(R.string.card_servings),
                recipe.getServings()));

        Picasso.with(mListener.getContext())
                .load(recipe.getImage().isEmpty() ? "error" : recipe.getImage())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.recipe_placeholder)
                .error(R.drawable.recipe_placeholder)
                .into(recipeViewHolder.photo);

        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRecipeClicked(recipe);
            }
        });
    }

    public void setItems(List<Recipe> items) {
        mRecipes = items;
        notifyItemRangeInserted(0, items.size() - 1);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recipe_name) TextView name;
        @BindView(R.id.tv_recipe_difficulty) TextView difficulty;
        @BindView(R.id.tv_ingredients) TextView ingredientsCount;
        @BindView(R.id.tv_servings) TextView servings;
        @BindView(R.id.iv_photo) ImageView photo;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
