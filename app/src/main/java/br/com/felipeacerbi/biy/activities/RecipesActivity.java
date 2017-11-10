package br.com.felipeacerbi.biy.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.utils.Constants;
import br.com.felipeacerbi.biy.utils.IdlingResourceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity implements IdlingResourceManager {

    @Nullable
    private CountingIdlingResource mIdlingResource;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @VisibleForTesting
    @Override
    public CountingIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new CountingIdlingResource(Constants.TEST_RECIPES_IDLING_RESOURCE);
        }
        return mIdlingResource;
    }

    @Override
    public void incrementIdlingResource() {
        getIdlingResource().increment();
    }

    @Override
    public void decrementIdlingResource() {
        getIdlingResource().decrement();
    }
}