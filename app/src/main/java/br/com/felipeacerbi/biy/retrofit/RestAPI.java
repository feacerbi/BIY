package br.com.felipeacerbi.biy.retrofit;

import java.util.List;

import javax.inject.Inject;

import br.com.felipeacerbi.biy.models.Recipe;
import retrofit2.Call;

@SuppressWarnings({"UnusedDeclaration"})
public class RestAPI {

    @Inject
    RecipesApiService recipesApiService;

    @Inject
    RestAPI() {}

    public Call<List<Recipe>> getRecipes() {
        return recipesApiService.getRecipes();
    }

}
