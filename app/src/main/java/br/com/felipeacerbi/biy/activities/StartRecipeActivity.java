package br.com.felipeacerbi.biy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.parceler.Parcels;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.adapters.IChangeStepListener;
import br.com.felipeacerbi.biy.adapters.RecipeStepsPagerAdapter;
import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StartRecipeActivity extends AppCompatActivity implements IChangeStepListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private RecipeStepsPagerAdapter mPagerAdapter;
    private Recipe mRecipe;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.fab_next)
    FloatingActionButton fabNext;
    @BindView(R.id.fab_ingredients)
    FloatingActionButton fabIngredients;
    @BindView(R.id.fab_previous)
    FloatingActionButton fabPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_recipe);

        handleIntent();
        setUpUI();
    }

    private void setUpUI() {
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setTitle(mRecipe.getName());
        mToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPagerAdapter = new RecipeStepsPagerAdapter(getSupportFragmentManager(), mRecipe.getSteps());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int currentStep = mViewPager.getCurrentItem();

                if(currentStep == mRecipe.getSteps().size() - 1) {
                    fabNext.setVisibility(View.INVISIBLE);
                } else if(currentStep == 0){
                    fabPrevious.setVisibility(View.INVISIBLE);
                } else {
                    fabNext.setVisibility(View.VISIBLE);
                    fabPrevious.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void handleIntent() {
        Intent startIntent = getIntent();
        if(startIntent.hasExtra(Constants.RECIPE_EXTRA)) {
            mRecipe = Parcels.unwrap(startIntent.getParcelableExtra(Constants.RECIPE_EXTRA));
        }
    }

    @Override
    public void goToNextStep() {
        int currentStep = mViewPager.getCurrentItem();
        currentStep++;
        mViewPager.setCurrentItem(currentStep, true);
    }

    @Override
    public void goToPreviousStep() {
        int currentStep = mViewPager.getCurrentItem();
        currentStep--;
        mViewPager.setCurrentItem(currentStep, true);
    }

    @Override
    public void openIngredients() {
        Intent ingredientsIntent = new Intent(this, IngredientsActivity.class);
        ingredientsIntent.putExtra(Constants.RECIPE_EXTRA, Parcels.wrap(mRecipe));
        startActivity(ingredientsIntent);
    }
}
