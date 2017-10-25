package br.com.felipeacerbi.biy.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import br.com.felipeacerbi.biy.R;
import br.com.felipeacerbi.biy.media.MediaPlayer;
import br.com.felipeacerbi.biy.models.Step;
import br.com.felipeacerbi.biy.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class StepFragment extends Fragment {

    TextView tvStepId;
    TextView tvShortDescription;

    @BindView(R.id.iv_step_photo)
    ImageView ivPhoto;
    @BindView(R.id.sepv_step_video_player)
    SimpleExoPlayerView sepvPlayerView;
    @BindView(R.id.tv_step_description)
    TextView tvDescription;

    @State(Step.class) Step mStep;
    private MediaPlayer mPlayer;
//    private IChangeStepListener mListener;


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

        setUpCommonUI(view);

        if(ButterKnife.findById(view, R.id.tv_step_id) != null) {
            setUpMobileUI(view);
        }
    }

    private void setUpMobileUI(View view) {
        tvStepId = ButterKnife.findById(view, R.id.tv_step_id);
        tvShortDescription = ButterKnife.findById(view, R.id.tv_step_short_description);

        tvStepId.setText(String.valueOf(mStep.getId() + 1));
        tvShortDescription.setText(mStep.getShortDescription());
    }

    private void setUpCommonUI(View view) {
        ButterKnife.bind(this, view);

        tvDescription.setText(formatText(mStep.getDescription()));

        if(!mStep.getVideoURL().isEmpty()) {
            sepvPlayerView.setVisibility(View.VISIBLE);
            ivPhoto.setVisibility(View.INVISIBLE);

            mPlayer = new MediaPlayer(getContext(), sepvPlayerView, Uri.parse(mStep.getVideoURL()), getLifecycle());

            if(getResources().getConfiguration().orientation == Constants.ORIENTATION_LANDSCAPE) {

                ConstraintLayout layout = ButterKnife.findById(view, R.id.cl_layout_no_video);
                if(layout != null) layout.setVisibility(View.INVISIBLE);

            }

        } else if(!mStep.getThumbnailURL().isEmpty()) {
            ivPhoto.setVisibility(View.VISIBLE);

            Picasso.with(getActivity())
                    .load(mStep.getThumbnailURL())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.recipe_placeholder)
                    .error(R.drawable.recipe_placeholder)
                    .into(ivPhoto);
        } else {
            FrameLayout mediaLayout = ButterKnife.findById(view, R.id.fl_media_container);
            if(mediaLayout != null) {
                mediaLayout.setVisibility(View.GONE);
            }
        }
    }

    private String formatText(String textToFormat) {
        textToFormat = textToFormat.trim();

        if(textToFormat.charAt(textToFormat.length() - 1) != '.') {
            textToFormat += ".";
        }

        if(textToFormat.charAt(1) == '.') {
            textToFormat = textToFormat.substring(3);
        }

        if(textToFormat.charAt(2) == '.') {
            textToFormat = textToFormat.substring(4);
        }

        return textToFormat;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof IChangeStepListener) {
//            mListener = (IChangeStepListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement IRecipeClickListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }


}
