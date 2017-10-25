package br.com.felipeacerbi.biy.adapters;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.activities.IngredientsActivity;
import br.com.felipeacerbi.biy.activities.StartRecipeActivity;
import br.com.felipeacerbi.biy.adapters.listeners.IRecipeClickListener;
import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter {

    private List<Recipe> mRecipes;
    private IRecipeClickListener mListener;

    public RecipesAdapter(List<Recipe> mRecipes, IRecipeClickListener mListener) {
        this.mRecipes = mRecipes;
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Recipe recipe = mRecipes.get(position);
        final RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;

        recipeViewHolder.name.setText(recipe.getName());

        Pair<String, Integer> difficulty = getRecipeDifficulty(recipe);
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

        recipeViewHolder.ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.getContext().startActivity(prepareRecipeIntent(IngredientsActivity.class, recipe));
            }
        });

        recipeViewHolder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.getContext().startActivity(prepareRecipeIntent(StartRecipeActivity.class, recipe));
            }
        });

    }

    private Intent prepareRecipeIntent(Class clazz, Recipe recipe) {
        Intent intent = new Intent(mListener.getContext(), clazz);
        intent.putExtra(Constants.RECIPE_EXTRA, Parcels.wrap(recipe));
        return intent;
    }

    private Pair<String, Integer> getRecipeDifficulty(Recipe recipe) {
        int stepsCount = recipe.getSteps().size();

        if (stepsCount <= mListener.getContext().getResources().getInteger(R.integer.easy_recipe_steps_count)) {
            return new Pair<String, Integer>(mListener.getContext().getString(R.string.easy_recipe), mListener.getContext().getResources().getColor(R.color.easy_green, mListener.getContext().getTheme()));
        } else if (stepsCount <= mListener.getContext().getResources().getInteger(R.integer.medium_recipe_steps_count)) {
            return new Pair<String, Integer>(mListener.getContext().getString(R.string.medium_recipe), mListener.getContext().getResources().getColor(R.color.medium_yellow, mListener.getContext().getTheme()));
        } else {
            return new Pair<String, Integer>(mListener.getContext().getString(R.string.hard_recipe), mListener.getContext().getResources().getColor(R.color.hard_red, mListener.getContext().getTheme()));
        }
    }

    public void setItems(List<Recipe> items) {
        mRecipes = items;
        notifyDataSetChanged();
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
        @BindView(R.id.btn_start_recipe) Button start;
        @BindView(R.id.btn_show_ingredients) Button ingredients;
        @BindView(R.id.iv_photo) ImageView photo;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
