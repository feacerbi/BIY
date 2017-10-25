package br.com.felipeacerbi.biy.adapters.listeners;

import android.content.Context;

import br.com.felipeacerbi.biy.models.Step;

public interface IRecipeStepClickListener {
    void onRecipeStepClicked(Step step);
    Context getContext();
}
