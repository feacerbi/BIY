package br.com.felipeacerbi.biy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.adapters.RecipeStepsAdapter;
import br.com.felipeacerbi.biy.adapters.listeners.IRecipeStepClickListener;
import br.com.felipeacerbi.biy.models.Step;
import br.com.felipeacerbi.biy.models.StepsArrayList;
import br.com.felipeacerbi.biy.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class StepsListFragment extends Fragment implements IRecipeStepClickListener {

    @BindView(R.id.rv_recipe_steps_list)
    RecyclerView mStepsList;

    @State(StepsArrayList.class)
    StepsArrayList mSteps;

    private IRecipeStepClickListener mListener;
    private RecipeStepsAdapter mAdapter;

    public StepsListFragment() {
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

        return inflater.inflate(R.layout.fragment_steps_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();

        if(arguments != null) {
            mSteps = Parcels.unwrap(arguments.getParcelable(Constants.STEPS_EXTRA));
        }
        
        setUpUI(view);
    }

    private void setUpUI(View view) {
        ButterKnife.bind(this, view);

        mAdapter = new RecipeStepsAdapter(new ArrayList<Step>(), this);
        mStepsList.setAdapter(mAdapter);

        setUpAdapter(mSteps);
    }

    private void setUpAdapter(List<Step> steps) {
        mAdapter.setItems(steps);
    }

    @Override
    public void onRecipeStepClicked(Step step) {
        if (mListener != null) {
            mListener.onRecipeStepClicked(step);
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
        if (context instanceof IRecipeStepClickListener) {
            mListener = (IRecipeStepClickListener) context;
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
