package br.com.felipeacerbi.biy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import br.com.felipeacerbi.biy.models.Recipe;

public abstract class PreferencesUtils {

    public static void storeRecipes(Context context, List<Recipe> recipes) {
        if(context != null) {
            JsonHelper jsonHelper = new JsonHelper();
            String json = jsonHelper.getJsonFromRecipes(recipes);

            if (json != null) {
                getPreferences(context)
                        .edit()
                        .putString(Constants.JSON_PREFERENCE, json)
                        .apply();
            }
        }
    }

    public static List<Recipe> loadRecipes(Context context) {
        if(context != null) {
            JsonHelper jsonHelper = new JsonHelper();

            String json = getPreferences(context).getString(Constants.JSON_PREFERENCE, "");

            return jsonHelper.getRecipesFromJson(json);
        }

        return null;
    }

    public static void storeRecipeId(Context context, int appWidgetId, int recipeId) {
        if(context != null) {
                getPreferences(context)
                        .edit()
                        .putInt(Constants.RECIPE_ID_PREFERENCE + appWidgetId, recipeId)
                        .apply();
        }
    }

    public static int loadRecipeId(Context context, int appWidgetId) {
        if(context != null) {
            return getPreferences(context)
                    .getInt(
                            Constants.RECIPE_ID_PREFERENCE + appWidgetId,
                            Constants.INVALID_RECIPE_ID);
        }

        return Constants.INVALID_RECIPE_ID;
    }

    public static void removeRecipeId(Context context, int appWidgetId) {
        if(context != null) {
            getPreferences(context)
                    .edit()
                    .remove(Constants.RECIPE_ID_PREFERENCE + appWidgetId)
                    .apply();
        }
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
    }
}
