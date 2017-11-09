package br.com.felipeacerbi.biy.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.com.felipeacerbi.biy.models.Recipe;

public class JsonHelper {

    private Gson gson;

    public JsonHelper() {
        gson = new Gson();
    }

    public List<Recipe> getRecipesFromJson(String json) {
        if(json != null && !json.isEmpty()) {
            Type recipesListType = new TypeToken<Collection<Recipe>>(){}.getType();

            List<Recipe> recipes = gson.fromJson(json, recipesListType);
            Collections.sort(recipes);

            for(Recipe recipe : recipes) {
                Collections.sort(recipe.getIngredients());
            }

            return recipes;
        } else {
            return null;
        }
    }

    public String getJsonFromRecipes(List<Recipe> recipes) {
        if(recipes != null && !recipes.isEmpty()) {
            return gson.toJson(recipes);
        }

        return null;
    }

}
