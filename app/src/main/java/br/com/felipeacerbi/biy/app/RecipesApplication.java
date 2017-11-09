package br.com.felipeacerbi.biy.app;

import android.app.Application;

import br.com.felipeacerbi.biy.dagger.DaggerRecipesComponent;
import br.com.felipeacerbi.biy.dagger.RecipesComponent;

public class RecipesApplication extends Application {

    private RecipesComponent recipesComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        recipesComponent = DaggerRecipesComponent
                .builder()
                .build();
    }

    public RecipesComponent getRecipesComponent() {
        return recipesComponent;
    }
}
