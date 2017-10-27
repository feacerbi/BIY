package br.com.felipeacerbi.biy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.models.Ingredient;
import br.com.felipeacerbi.biy.utils.IngredientMeasures;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter {

    private List<Ingredient> mIngredients;

    public IngredientsAdapter(List<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Ingredient ingredient = mIngredients.get(position);
        final IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;

        String description = ingredient.getIngredient().toUpperCase().charAt(0) + ingredient.getIngredient().substring(1);

        ingredientViewHolder.description.setText(description);

        String quantiy = String.valueOf(ingredient.getQuantity());
        boolean isFraction = quantiy.contains(".") && quantiy.charAt(quantiy.lastIndexOf(".") + 1) != '0';

        ingredientViewHolder.quantity.setText(
                String.format(Locale.getDefault(),
                        (isFraction) ? "%.1f" : "%.0f",
                        ingredient.getQuantity()));

        ingredientViewHolder.unit.setText(
                IngredientMeasures.getFormattedMeasure(
                        ingredient.getMeasure(),
                        ingredient.getQuantity() > 1));
    }

    public void setItems(List<Ingredient> items) {
        mIngredients = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient_description) TextView description;
        @BindView(R.id.tv_ingredient_quantity) TextView quantity;
        @BindView(R.id.tv_ingredient_unit) TextView unit;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
