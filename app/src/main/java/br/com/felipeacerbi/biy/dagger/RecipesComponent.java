package br.com.felipeacerbi.biy.dagger;

import javax.inject.Singleton;

import br.com.felipeacerbi.biy.fragments.RecipesFragment;
import dagger.Component;

@Singleton
@Component(modules = {
        RecipesModule.class,
        RetrofitModule.class})

public interface RecipesComponent {
    void inject(RecipesFragment fragment);
}
