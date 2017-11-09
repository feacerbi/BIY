package br.com.felipeacerbi.biy.repository;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.retrofit.RestAPI;
import br.com.felipeacerbi.biy.utils.RequestCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
class RestDataSource {

    @Inject
    RestAPI restAPI;

    @Inject
    RestDataSource() {}

    void getRecipes(final RequestCallback<List<Recipe>> callback) {

        Call<List<Recipe>> recipes = restAPI.getRecipes();

        if (recipes != null) {
            recipes.enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                        if(response.isSuccessful()) {
                            List<Recipe> recipesResponse = response.body();
                            callback.onSuccess(recipesResponse);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
            });
        }
    }

}
