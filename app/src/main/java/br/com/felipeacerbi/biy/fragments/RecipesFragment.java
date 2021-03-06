package br.com.felipeacerbi.biy.fragments;

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

import javax.inject.Inject;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.adapters.RecipesAdapter;
import br.com.felipeacerbi.biy.app.RecipesApplication;
import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.models.RecipesArrayList;
import br.com.felipeacerbi.biy.repository.DataManager;
import br.com.felipeacerbi.biy.utils.IdlingResourceManager;
import br.com.felipeacerbi.biy.utils.PreferencesUtils;
import br.com.felipeacerbi.biy.utils.RequestCallback;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class RecipesFragment extends Fragment {

    @BindView(R.id.rv_recipes_list)
    RecyclerView mRecipesList;

    @State(RecipesArrayList.class)
    RecipesArrayList mRecipes;

    @Inject
    DataManager mDataManager;

    private IdlingResourceManager mIdlingManager;
    private RecipesAdapter mAdapter;

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        ((RecipesApplication) getActivity().getApplicationContext()).getRecipesComponent().inject(this);
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

        mAdapter = new RecipesAdapter(getActivity(), new ArrayList<Recipe>());
        mRecipesList.setAdapter(mAdapter);

        if(mRecipes != null) {
            setUpAdapter(mRecipes);
        } else {
            requestNewRecipes();
        }
    }

    private void requestNewRecipes() {
        mIdlingManager.incrementIdlingResource();

        mDataManager.requestRecipes(new RequestCallback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                setUpAdapter(recipes);
                PreferencesUtils.storeRecipes(getActivity(), recipes);

                mIdlingManager.decrementIdlingResource();
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
    public Context getContext() {
        return getSupportActivity();
    }

    private AppCompatActivity getSupportActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IdlingResourceManager) {
            mIdlingManager = (IdlingResourceManager) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IdlingResourceManager");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIdlingManager = null;
    }
}
