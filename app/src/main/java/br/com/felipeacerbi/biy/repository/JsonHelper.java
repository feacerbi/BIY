package br.com.felipeacerbi.biy.repository;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.felipeacerbi.biy.models.Recipe;

public class JsonHelper {

    private static final String RECIPES_JSON_FILENAME = "baking.json";

    private Context context;

    public JsonHelper(Context context) {
        this.context = context;
    }

    public List<Recipe> getRecipes() {
        Gson gson = new Gson();

        String json = loadJSONFromAsset();

        if(json != null) {
            Type recipesListType = new TypeToken<Collection<Recipe>>(){}.getType();
            return gson.fromJson(json, recipesListType);
        } else {
            return null;
        }
    }

    private String loadJSONFromAsset() {

        String json;
        try {

            InputStream is = context.getAssets().open(RECIPES_JSON_FILENAME);

            int size = is.available();

            byte[] buffer = new byte[size];

            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Log.d("Help", json);
        return json;

    }

}
