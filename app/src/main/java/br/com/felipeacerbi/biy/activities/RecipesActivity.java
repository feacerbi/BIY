package br.com.felipeacerbi.biy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.com.felipeacerbi.biy.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

}