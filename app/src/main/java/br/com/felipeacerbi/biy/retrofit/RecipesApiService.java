package br.com.felipeacerbi.biy.retrofit;

import java.util.List;

import br.com.felipeacerbi.biy.models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipesApiService {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();

}
