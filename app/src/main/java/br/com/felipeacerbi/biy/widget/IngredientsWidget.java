package br.com.felipeacerbi.biy.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import org.parceler.Parcels;

import java.util.List;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.activities.RecipesActivity;
import br.com.felipeacerbi.biy.activities.StartRecipeActivity;
import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.repository.DataManager;
import br.com.felipeacerbi.biy.utils.Constants;
import br.com.felipeacerbi.biy.utils.RequestCallback;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidgetConfigureActivity IngredientsWidgetConfigureActivity}
 */
public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        DataManager dataManager = new DataManager(context);

        dataManager.requestRecipes(new RequestCallback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                int recipeId = IngredientsWidgetConfigureActivity.loadRecipePref(context, appWidgetId);
                if(recipeId != Constants.INVALID_RECIPE_ID) {
                    Recipe recipe = recipes.get(recipeId);

                    // Set up the intent that starts the StackViewService, which will
                    // provide the views for this collection.
                    Intent intent = new Intent(context, ListWidgetService.class);
                    // Add the app widget ID to the intent extras.
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

                    // Instantiate the RemoteViews object for the app widget layout.
                    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

                    // Set up the RemoteViews object to use a RemoteViews adapter.
                    // This adapter connects
                    // to a RemoteViewsService  through the specified intent.
                    // This is how you populate the data.
                    rv.setRemoteAdapter(R.id.lv_ingredients_list, intent);
                    // The empty view is displayed when the collection has no items.
                    // It should be in the same layout used to instantiate the RemoteViews
                    // object above.
                    rv.setEmptyView(R.id.lv_ingredients_list, R.id.tv_empty_ingredients);

                    rv.setTextViewText(R.id.tv_title, recipe.getName());

                    Intent startMainIntent = new Intent(context, RecipesActivity.class);
                    PendingIntent startMainPendingIntent = PendingIntent.getActivity(
                            context,
                            0,
                            startMainIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent startRecipeIntent = new Intent(context, StartRecipeActivity.class);
                    startRecipeIntent.putExtra(Constants.RECIPE_EXTRA, Parcels.wrap(recipe));
                    PendingIntent startRecipePendingIntent = PendingIntent.getActivity(
                            context,
                            appWidgetId,
                            startRecipeIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    rv.setOnClickPendingIntent(R.id.iv_start, startRecipePendingIntent);
                    rv.setOnClickPendingIntent(R.id.tv_title, startMainPendingIntent);
                    rv.setOnClickPendingIntent(R.id.tv_empty_ingredients, startMainPendingIntent);

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, rv);
                }
            }

            @Override
            public void onError(String error) {
                Log.d("Widget", "Error: " + error);
            }
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidgetConfigureActivity.deleteRecipePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

