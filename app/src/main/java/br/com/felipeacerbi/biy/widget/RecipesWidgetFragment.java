package br.com.felipeacerbi.biy.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.models.RecipesArrayList;
import br.com.felipeacerbi.biy.repository.DataManager;
import br.com.felipeacerbi.biy.utils.RequestCallback;
import br.com.felipeacerbi.biy.widget.listeners.IRecipeClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class RecipesWidgetFragment extends Fragment implements IRecipeClickListener {

    @BindView(R.id.rv_recipes_list)
    RecyclerView mRecipesList;

    @State(RecipesArrayList.class)
    RecipesArrayList mRecipes;

    private IRecipeClickListener mListener;
    private RecipesWidgetAdapter mAdapter;
    private DataManager mDataManager;

    public RecipesWidgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recipes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setUpUI(view);
    }

    private void setUpUI(View view) {
        ButterKnife.bind(this, view);

        mAdapter = new RecipesWidgetAdapter(new ArrayList<Recipe>(), this);
        mRecipesList.setAdapter(mAdapter);

        mDataManager = new DataManager(getSupportActivity());

        if(mRecipes != null) {
            setUpAdapter(mRecipes);
        } else {
            requestNewRecipes();
        }
    }

    private void requestNewRecipes() {
        mDataManager.requestRecipes(new RequestCallback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                setUpAdapter(recipes);
            }

            @Override
            public void onError(String error) {
                Log.d("Frag", "Error: " + error);
            }
        });
    }

    private void setUpAdapter(List<Recipe> recipes) {
        mRecipes = new RecipesArrayList(recipes);
        mAdapter.setItems(recipes);
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        if (mListener != null) {
            mListener.onRecipeClicked(recipe);
        }
    }

    @Override
    public Context getContext() {
        return getSupportActivity();
    }

    private AppCompatActivity getSupportActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IRecipeClickListener) {
            mListener = (IRecipeClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IRecipeClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
