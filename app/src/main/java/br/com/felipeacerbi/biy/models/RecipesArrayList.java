package br.com.felipeacerbi.biy.models;

import android.os.Bundle;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;

import icepick.Bundler;

@Parcel
public class RecipesArrayList extends ArrayList<Recipe> implements Bundler<ArrayList<Recipe>> {

    public RecipesArrayList() {
    }

    @Override
    public void put(String key, ArrayList<Recipe> recipes, Bundle bundle) {
        bundle.putParcelable(key, Parcels.wrap(recipes));
    }

    @Override
    public ArrayList<Recipe> get(String key, Bundle bundle) {
        return Parcels.unwrap(bundle.getParcelable(key));
    }
}
