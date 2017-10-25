package br.com.felipeacerbi.biy.models;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;

import icepick.Bundler;

@Parcel
public class StepsArrayList extends ArrayList<Step> implements Bundler<ArrayList<Step>> {

    public StepsArrayList() {
    }

    public StepsArrayList(@NonNull Collection<? extends Step> c) {
        super(c);
    }

    @Override
    public void put(String key, ArrayList<Step> steps, Bundle bundle) {
        bundle.putParcelable(key, Parcels.wrap(steps));
    }

    @Override
    public ArrayList<Step> get(String key, Bundle bundle) {
        return Parcels.unwrap(bundle.getParcelable(key));
    }
}
