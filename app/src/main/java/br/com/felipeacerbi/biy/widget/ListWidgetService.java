package br.com.felipeacerbi.biy.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;
import java.util.Locale;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.models.Ingredient;
import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.utils.IngredientMeasures;
import br.com.felipeacerbi.biy.utils.PreferencesUtils;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mRecipeId;
    private Recipe mRecipe;

    ListRemoteViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
        int mAppWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mRecipeId = PreferencesUtils.loadRecipeId(mContext, mAppWidgetId);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        List<Recipe> recipes = PreferencesUtils.loadRecipes(mContext);
        mRecipe = recipes.get(mRecipeId);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mRecipe == null) return 0;
        return mRecipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Ingredient ingredient = mRecipe.getIngredients().get(i);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget_list_item);

        String description = ingredient.getIngredient().toUpperCase().charAt(0) + ingredient.getIngredient().substring(1);
        String quantiy = String.valueOf(ingredient.getQuantity());
        boolean isFraction = quantiy.contains(".") && quantiy.charAt(quantiy.lastIndexOf(".") + 1) != '0';

        rv.setTextViewText(R.id.tv_ingredient_description, description);

        rv.setTextViewText(R.id.tv_ingredient_quantity,
                String.format(Locale.getDefault(),
                (isFraction) ? "%.1f" : "%.0f",
                ingredient.getQuantity()));

        rv.setTextViewText(R.id.tv_ingredient_unit,
                IngredientMeasures.getFormattedMeasure(
                ingredient.getMeasure(),
                ingredient.getQuantity() > 1));

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
