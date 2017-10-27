package br.com.felipeacerbi.biy.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.adapters.listeners.IRecipeClickListener;
import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.utils.Constants;

/**
 * The configuration screen for the {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigureActivity extends AppCompatActivity implements IRecipeClickListener {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public IngredientsWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_ingredients_widget_configure);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        saveRecipePref(this, mAppWidgetId, recipe.getId());

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
        IngredientsWidget.updateAppWidget(this, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    static void saveRecipePref(Context context, int appWidgetId, int recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0).edit();
        prefs.putInt(Constants.PREF_RECIPE_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadRecipePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        return prefs.getInt(Constants.PREF_RECIPE_KEY + appWidgetId, 0);
    }

    static void deleteRecipePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0).edit();
        prefs.remove(Constants.PREF_RECIPE_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public Context getContext() {
        return this;
    }
}

