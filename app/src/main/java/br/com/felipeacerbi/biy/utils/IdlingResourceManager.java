package br.com.felipeacerbi.biy.utils;

import android.support.test.espresso.idling.CountingIdlingResource;

public interface IdlingResourceManager {
    CountingIdlingResource getIdlingResource();
    void incrementIdlingResource();
    void decrementIdlingResource();
}
