package br.com.felipeacerbi.biy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.parceler.Parcels;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.adapters.IngredientsAdapter;
import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_ingredients_list)
    RecyclerView mIngredientsList;

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        handleIntent();
        setUpUI();
    }

    private void setUpUI() {
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        String title = mRecipe.getName();
        if(title.charAt(title.length() - 1) == 's') {
            title += getString(R.string.ingredients_title_plural);
        } else {
            title += getString(R.string.ingredients_title_singular);
        }

        mToolbar.setTitle(title);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        IngredientsAdapter adapter = new IngredientsAdapter(mRecipe.getIngredients());
        mIngredientsList.setAdapter(adapter);
    }

    private void handleIntent() {
        Intent startIntent = getIntent();
        if(startIntent.hasExtra(Constants.RECIPE_EXTRA)) {
            mRecipe = Parcels.unwrap(startIntent.getParcelableExtra(Constants.RECIPE_EXTRA));
        }
    }

}