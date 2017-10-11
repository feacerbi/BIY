package br.com.felipeacerbi.biy.adapters;

import android.content.Context;

import br.com.felipeacerbi.biy.models.Recipe;

public interface IChangeStepListener {
    void goToNextStep();
    void goToPreviousStep();
    void openIngredients();
}
