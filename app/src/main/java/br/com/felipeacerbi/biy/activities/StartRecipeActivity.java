package br.com.felipeacerbi.biy.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.parceler.Parcels;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.adapters.RecipeStepsPagerAdapter;
import br.com.felipeacerbi.biy.adapters.listeners.IRecipeStepClickListener;
import br.com.felipeacerbi.biy.fragments.StepFragment;
import br.com.felipeacerbi.biy.fragments.StepsListFragment;
import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.models.Step;
import br.com.felipeacerbi.biy.models.StepsArrayList;
import br.com.felipeacerbi.biy.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import tabi.vpindicator.ViewPagerIndicator;

public class StartRecipeActivity extends AppCompatActivity implements IRecipeStepClickListener {

    private Recipe mRecipe;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @Nullable @BindView(R.id.vp_steps)
    ViewPager mViewPager;
    @BindView(R.id.vpi_container_indicator)
    ViewPagerIndicator mViewPagerIndicator;
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

        setUpCommonUI();

        if(mViewPager != null) {
            setUpMobileUI();
        } else {
            setUpTabletUI();
        }
    }

    private void setUpCommonUI() {
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

        fabIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIngredients();
            }
        });
    }

    private void setUpMobileUI() {
        ButterKnife.bind(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        RecipeStepsPagerAdapter mPagerAdapter = new RecipeStepsPagerAdapter(getSupportFragmentManager(), mRecipe.getSteps());

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                fabNext.setImageResource(R.drawable.ic_arrow_forward_black_24dp);

                if(position == mRecipe.getSteps().size() - 1) {
                    fabNext.setImageResource(R.drawable.ic_restaurant_menu_white_24dp);
                } else if(position == 0){
                    fabNext.show();
                    fabPrevious.hide();
                } else {
                    fabNext.show();
                    fabPrevious.show();
                }

                fabIngredients.show();

                checkFullScreen();
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        };

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        mViewPagerIndicator.setViewPager(mViewPager);

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextStep();
            }
        });
        fabPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPreviousStep();
            }
        });

        checkFullScreen();
    }

    private void setUpTabletUI() {
        StepsListFragment stepsFragment = new StepsListFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.STEPS_EXTRA, Parcels.wrap(new StepsArrayList(mRecipe.getSteps())));
        stepsFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_steps_list_fragment, stepsFragment)
                .commit();

        selectStep(mRecipe.getSteps().get(0));
    }

    private void selectStep(Step step) {
        StepFragment stepFragment = new StepFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.STEP_EXTRA, Parcels.wrap(step));
        stepFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_step_fragment, stepFragment)
                .commit();
    }

    private void checkFullScreen() {
        if(getResources().getConfiguration().orientation == Constants.ORIENTATION_LANDSCAPE
                && mRecipe.getSteps().get(mViewPager.getCurrentItem()).hasVideo()) {
            goFullScreen();
        } else {
            exitFullScreen();
        }
    }

    private void handleIntent() {
        Intent startIntent = getIntent();
        if(startIntent.hasExtra(Constants.RECIPE_EXTRA)) {
            mRecipe = Parcels.unwrap(startIntent.getParcelableExtra(Constants.RECIPE_EXTRA));
        }
    }

    public void goToNextStep() {
        int currentStep = mViewPager.getCurrentItem();

        if(currentStep == mRecipe.getSteps().size() - 1) {
            openFinishDialog();
        } else {
            currentStep++;
            mViewPager.setCurrentItem(currentStep, true);
        }
    }

    public void goToPreviousStep() {
        int currentStep = mViewPager.getCurrentItem();
        currentStep--;
        mViewPager.setCurrentItem(currentStep, true);
    }

    public void openIngredients() {
        Intent ingredientsIntent = new Intent(this, IngredientsActivity.class);
        ingredientsIntent.putExtra(Constants.RECIPE_EXTRA, Parcels.wrap(mRecipe));
        startActivity(ingredientsIntent);
    }

    public void goFullScreen() {
        fabIngredients.hide();
        fabPrevious.hide();
        fabNext.hide();

        mViewPagerIndicator.setVisibility(View.GONE);

        if(getSupportActionBar() != null) getSupportActionBar().hide();

        mainContent.setFitsSystemWindows(false);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void exitFullScreen() {
        mViewPagerIndicator.setVisibility(View.VISIBLE);
        if(getSupportActionBar() != null) getSupportActionBar().show();

        mainContent.setFitsSystemWindows(true);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void openFinishDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.finish_cooking_message)
                .setPositiveButton(R.string.affirmative_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(StartRecipeActivity.this, R.string.finish_recipe_toast_message, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .setNegativeButton(R.string.negative_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRecipeStepClicked(Step step) {
        selectStep(step);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
