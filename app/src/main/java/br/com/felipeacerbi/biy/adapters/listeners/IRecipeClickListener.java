package br.com.felipeacerbi.biy.adapters.listeners;

import android.content.Context;

import br.com.felipeacerbi.biy.models.Recipe;

public interface IRecipeClickListener {
    void onRecipeClicked(Recipe recipe);
    Context getContext();
}
