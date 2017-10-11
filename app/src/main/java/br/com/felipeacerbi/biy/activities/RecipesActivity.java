package br.com.felipeacerbi.biy.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import br.com.felipeacerbi.biy.adapters.IRecipeClickListener;
import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.models.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity implements IRecipeClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        Toast.makeText(this, "Recipe clicked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

}