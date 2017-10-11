package br.com.felipeacerbi.biy.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.adapters.IChangeStepListener;
import br.com.felipeacerbi.biy.adapters.IRecipeClickListener;
import br.com.felipeacerbi.biy.adapters.RecipesAdapter;
import br.com.felipeacerbi.biy.models.Recipe;
import br.com.felipeacerbi.biy.models.RecipesArrayList;
import br.com.felipeacerbi.biy.models.Step;
import br.com.felipeacerbi.biy.repository.DataManager;
import br.com.felipeacerbi.biy.utils.Constants;
import br.com.felipeacerbi.biy.utils.RequestCallback;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import icepick.Icepick;
import icepick.State;

public class StepFragment extends Fragment {

    @BindView(R.id.tv_step_id)
    TextView tvStepId;
    @BindView(R.id.tv_step_short_description)
    TextView tvShortDescription;
    @BindView(R.id.iv_step_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_step_description)
    TextView tvDescription;

    FloatingActionButton fabPrevious;
    FloatingActionButton fabIngredients;
    FloatingActionButton fabNext;

    @State(Step.class) Step mStep;
    private IChangeStepListener mListener;

    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_start_recipe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();

        if(arguments != null) {
            mStep = Parcels.unwrap(arguments.getParcelable(Constants.STEP_EXTRA));
        }

        setUpUI(view);
    }

    private void setUpUI(View view) {
        ButterKnife.bind(this, view);

        tvStepId.setText(String.valueOf(mStep.getId() + 1));
        tvShortDescription.setText(mStep.getShortDescription());

        String description = mStep.getDescription();
        tvDescription.setText((description.charAt(1) == '.') ? description.substring(3) : description);

//        if(mStep.getThumbnailURL().isEmpty()) {
//            ivPhoto.setVisibility(View.GONE);
//        } else {
            ivPhoto.setVisibility(View.VISIBLE);

            Picasso.with(getActivity())
                    .load("error")//mStep.getThumbnailURL())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.recipe_placeholder)
                    .error(R.drawable.recipe_placeholder)
                    .into(ivPhoto);
//        }

        fabIngredients = ButterKnife.findById(getActivity(), R.id.fab_ingredients);
        fabIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.openIngredients();
            }
        });

        fabNext = ButterKnife.findById(getActivity(), R.id.fab_next);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToNextStep();
            }
        });

        fabPrevious = ButterKnife.findById(getActivity(), R.id.fab_previous);
        fabPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToPreviousStep();
            }
        });

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
        if (context instanceof IChangeStepListener) {
            mListener = (IChangeStepListener) context;
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
