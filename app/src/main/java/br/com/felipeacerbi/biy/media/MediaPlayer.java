package br.com.felipeacerbi.biy.media;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class MediaPlayer implements LifecycleObserver {

    private static final String SAVE_POSITION_KEY = "position";
    private static final String SAVE_WINDOW_KEY = "window";
    private static final String SAVE_SHOULD_PLAY_KEY = "shouldPlay";

    private Context mContext;

    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mView;
    private Uri mVideoUri;

    private long mPlaybackPosition = C.TIME_UNSET;
    private int mCurrentWindow = C.INDEX_UNSET;
    private boolean mShouldPlay = false;

    public MediaPlayer(Context context, SimpleExoPlayerView view, Uri videoUri, @Nullable Lifecycle lifecycle) {
        mContext = context;
        mView = view;
        mVideoUri = videoUri;

        if(lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        initializePlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        releasePlayer();
    }

    public Bundle createSaveBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(SAVE_POSITION_KEY, mPlaybackPosition);
        bundle.putInt(SAVE_WINDOW_KEY, mCurrentWindow);
        bundle.putBoolean(SAVE_SHOULD_PLAY_KEY, mShouldPlay);
        return bundle;
    }

    public void restoreSaveBundle(Bundle bundle) {
        mPlaybackPosition = bundle.getLong(SAVE_POSITION_KEY);
        mCurrentWindow = bundle.getInt(SAVE_WINDOW_KEY);
        mShouldPlay = bundle.getBoolean(SAVE_SHOULD_PLAY_KEY);
    }

    private void createPlayer() {
        mPlayer =  ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mContext),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
    }

    private void preparePlayer() {
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(mVideoUri,
                new DefaultHttpDataSourceFactory("BIY"),
                new DefaultExtractorsFactory(), null, null);

        // Prepare the player with the source.
        mPlayer.prepare(videoSource, true, false);
    }

    private void initializePlayer() {
        if(mView.getVisibility() == View.VISIBLE) {
            createPlayer();
            preparePlayer();

            mView.setPlayer(mPlayer);
            mPlayer.setPlayWhenReady(mShouldPlay);

            if(mCurrentWindow != C.INDEX_UNSET && mPlaybackPosition != C.TIME_UNSET) {
                mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
            }
        }
    }

    private void releasePlayer() {
        if(mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mShouldPlay = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void clearPlayer() {
        mPlaybackPosition = C.TIME_UNSET;
        mCurrentWindow = C.INDEX_UNSET;
    }
}
