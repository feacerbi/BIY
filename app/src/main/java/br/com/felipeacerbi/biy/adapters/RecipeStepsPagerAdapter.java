package br.com.felipeacerbi.biy.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.parceler.Parcels;

import java.util.List;

import br.com.felipeacerbi.biy.fragments.StepFragment;
import br.com.felipeacerbi.biy.models.Step;
import br.com.felipeacerbi.biy.utils.Constants;

public class RecipeStepsPagerAdapter extends FragmentStatePagerAdapter {

    private List<Step> mSteps;

    public RecipeStepsPagerAdapter(FragmentManager fm, List<Step> steps) {
        super(fm);
        this.mSteps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        StepFragment stepFragment = new StepFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.STEP_EXTRA, Parcels.wrap(mSteps.get(position)));
        stepFragment.setArguments(args);

        return stepFragment;
    }

    @Override
    public int getCount() {
        return mSteps.size();
    }
}
