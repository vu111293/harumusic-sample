package com.mitek.exomultiquality.player;

import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mitek.exomultiquality.R;
import com.mitek.exomultiquality.models.YtFragmentedVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HexoPlayer extends RelativeLayout {

    private static final String TAG = HexoPlayer.class.getSimpleName();

    //Minimum Video you want to buffer while Playing
    public static final int MIN_BUFFER_DURATION = 2000;
    //Max Video you want to buffer during PlayBack
    public static final int MAX_BUFFER_DURATION = 5000;
    //Min Video you want to buffer before start Playing it
    public static final int MIN_PLAYBACK_START_BUFFER = 1500;
    //Min video You want to buffer when user resumes video
    public static final int MIN_PLAYBACK_RESUME_BUFFER = 2000;

    private static final int GOOD_QUALITY_DEFAULT = 480;

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private View processBar;
    private TextView speedTv;
    private TextView qualityTv;
    private ImageView fullscreenButton;
    private boolean fullscreen = false;
    private float mSpeed = 1.0f;
    private AppCompatActivity act;
//    private int mResumeWindow;
//    private long mResumePosition;
    private List<YtFragmentedVideo> videoClips = new ArrayList<>();
    private long videoPosition = 0;
    private boolean isPIPModeeEnabled = true;
    public boolean isInPipMode = false;
    private boolean readyForClose = false;


    private LayoutInflater mInflater;

    public HexoPlayer(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();

    }
    public HexoPlayer(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        init();
    }
    public HexoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }
    public void init()
    {
        View v = mInflater.inflate(R.layout.hexoplayer, this, true);
        v.findViewById(R.id.exo_speed_text).setOnClickListener(mOnClick);
        v.findViewById(R.id.exo_fullscreen_icon).setOnClickListener(mOnClick);
        v.findViewById(R.id.quality_options).setOnClickListener(mOnClick);
        v.findViewById(R.id.exo_pip_icon).setOnClickListener(mOnClick);

        playerView = (PlayerView) v.findViewById(R.id.playerView);
        processBar = v.findViewById(R.id.progressBar);
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        qualityTv = (TextView) playerView.findViewById(R.id.quality_options);
        speedTv = (TextView) playerView.findViewById(R.id.exo_speed_text);
    }

    public void setActivity(AppCompatActivity act) {
        this.act = act;
    }

    public void setVideos(List<YtFragmentedVideo> clips) {
        this.videoClips = clips;
    }

//    public void pause() {
//        if (playerView != null && player != null) {
//            player.setPlayWhenReady(false);
//            mResumeWindow = player.getCurrentWindowIndex();
//            mResumePosition = Math.max(0, player.getContentPosition());
////            if (Util.SDK_INT <= 23) releasePlayer();
//        }
//    }

    public void release() {
        releasePlayer();
    }

    public void play() {
        if (videoClips.size() > 0) {
            YtFragmentedVideo clip = getBestQuality(); // videoClips.get(videoClips.size() - 1);
            playFromVideoExtract(clip);
        } else {
            Toast.makeText(act, "Fetch youtube failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setReadyForClose(boolean isReady) {
        readyForClose = isReady;
    }

    public int playerWidth() {
        return playerView.getWidth();
    }

    public int playerHeight() {
        return playerView.getHeight();
    }

    public long getVideoPosition() {
        return player.getCurrentPosition();
    }

    public void setVideoPosition(long p) {
        videoPosition = p;
    }

    private View.OnClickListener mOnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.exo_speed_text:
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        boolean supportsPIP = act.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
//                        if (supportsPIP)
//                            act.enterPictureInPictureMode();
//                    } else {
//                        new androidx.appcompat.app.AlertDialog.Builder(getContext())
//                                .setTitle("Can't enter picture in picture mode")
//                                .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
//                                .show();
//                    }

                    changeSpeed();
                    break;

                case R.id.exo_fullscreen_icon:
                    toggleFullscreen();
                    break;

                case R.id.quality_options:
                    showQualityOptions(view);
                    break;

                case R.id.exo_pip_icon:
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                            && act.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
                            && isPIPModeeEnabled) {
                        enterPIPMode();
                    } else {
                        Toast.makeText(act, "PIP not supported", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    break;

            }
        }
    };

    public void playFromFile(String path) {
        qualityTv.setText("local");
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(act, Util.getUserAgent(getContext(), "mediaPlayerSample"));
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.fromFile(new File(path)));
        playVideoFromMediaSource(mediaSource);
    }

    public void playFromVideoExtract(YtFragmentedVideo clip) {
        qualityTv.setText(clip.height + "p");
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(act, Util.getUserAgent(getContext(), "mediaPlayerSample"));
        ProgressiveMediaSource mediaVideoSource = new ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(clip.videoFile.url));
        ProgressiveMediaSource mediaAudioSource = new ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(clip.audioFile.url));
        MediaSource mediaSource = new MergingMediaSource(mediaVideoSource, mediaAudioSource);
        playVideoFromMediaSource(mediaSource);
    }


    public void onPagePause() {
        if (playerView != null && player != null) {
            videoPosition = Math.max(0, player.getContentPosition());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onPageResume() {
        if(videoPosition > 0L && isInPipMode) {
            player.seekTo(videoPosition);
        }
        //Makes sure that the media controls pop up on resuming and when going between PIP and non-PIP states.
        if (isInPipMode) {
            setFullScreen(true);
        } else {
            setFullScreen(fullscreen);
            playerView.setUseController(true);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onPipChange(Configuration newConfig) {
        if(newConfig != null){
            videoPosition = player.getCurrentPosition();
            isInPipMode = act.isInPictureInPictureMode();
            Log.d(TAG, "PIP mode: " + isInPipMode);

            if (readyForClose) {
                releasePlayer();
            }
        }
    }

    public void enterPIPMode(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && act.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
            videoPosition = player.getCurrentPosition();
//            enterFullScreen();
            setFullScreen(true);
            playerView.setUseController(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PictureInPictureParams.Builder params = new PictureInPictureParams.Builder();
                act.enterPictureInPictureMode(params.build());
            } else {
                act.enterPictureInPictureMode();
            }
            /* We need to check this because the system permission check is publically hidden for integers for non-manufacturer-built apps
               https://github.com/aosp-mirror/platform_frameworks_base/blob/studio-3.1.2/core/java/android/app/AppOpsManager.java#L1640

               ********* If we didn't have that problem *********
                val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                if(appOpsManager.checkOpNoThrow(AppOpManager.OP_PICTURE_IN_PICTURE, packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).uid, packageName) == AppOpsManager.MODE_ALLOWED)

                30MS window in even a restricted memory device (756mb+) is more than enough time to check, but also not have the system complain about holding an action hostage.
             */
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkPIPPermission();
                }
            }, 30);
        }
    }


    // private methods
    @RequiresApi(Build.VERSION_CODES.N)
    private void checkPIPPermission(){
        isPIPModeeEnabled = act.isInPictureInPictureMode();
        if(!isPIPModeeEnabled){
            // Todo not understand
//            onBackPressed()
        }
    }

    private Player.EventListener mMediaEventListener = new Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            print("Loading state: " + isLoading);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_BUFFERING:
                    print("buffering");
                    processBar.setVisibility(View.VISIBLE);
                    break;
                case Player.STATE_READY:
                    print("ready");
                    processBar.setVisibility(View.GONE);
                    break;
                case Player.STATE_ENDED:
                    print("end");
                    processBar.setVisibility(View.GONE);
                    break;
                case Player.STATE_IDLE:
                    print("IDLE");
                    processBar.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    };

    private void setFullScreen(boolean enable) {
        if (enable) {
            act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            if (act.getSupportActionBar() != null) {
                act.getSupportActionBar().hide();
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView.setLayoutParams(params);
        } else {
            act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if (act.getSupportActionBar() != null) {
                act.getSupportActionBar().show();
            }
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = (int) (200 * act.getApplicationContext().getResources().getDisplayMetrics().density);
            playerView.setLayoutParams(params);
        }
    }

    private void toggleFullscreen() {
        if (fullscreen) {
            setFullScreen(false);
            fullscreenButton.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_fullscreen_open));
            act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            fullscreen = false;
        } else {
            setFullScreen(true);
            fullscreenButton.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.ic_fullscreen_close));
            act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            fullscreen = true;
        }
    }

    private void changeSpeed() {
        if (player != null) {
            mSpeed = mSpeed + 0.5f;
            if (mSpeed > 3.0f) {
                mSpeed = 1.0f;
            }
            speedTv.setText("x" + mSpeed);
            PlaybackParameters param = new PlaybackParameters(mSpeed);
            player.setPlaybackParameters(param);
            Toast.makeText(act, "Speed change to : " + mSpeed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(act, "Player not ready", Toast.LENGTH_SHORT).show();
        }
    }


    private YtFragmentedVideo getBestQuality() {
        YtFragmentedVideo ret = null;
        for (int i = videoClips.size() - 1; i >= 0; --i) {
            if (videoClips.get(i).height < GOOD_QUALITY_DEFAULT) continue;
            ret = videoClips.get(i);
            break;
        }

        if (ret == null) {
            ret = videoClips.get(0);
        }
        return ret;
    }

    private void releasePlayer() {
        player.release();
    }

    private void playVideoFromMediaSource(MediaSource source) {
        if (playerView != null && player != null) {
            player.setPlayWhenReady(false);
//            mResumeWindow = player.getCurrentWindowIndex();
//            mResumePosition = Math.max(0, player.getContentPosition());
            videoPosition = Math.max(0, player.getContentPosition());
        }

        if (player != null) {
            player.stop();
            player.release();
        }

//        LoadControl loadControl = new DefaultLoadControl.Builder()
//                .setAllocator(new DefaultAllocator(true, 16))
//                .setBufferDurationsMs(MIN_BUFFER_DURATION,
//                        MAX_BUFFER_DURATION,
//                        MIN_PLAYBACK_START_BUFFER,
//                        MIN_PLAYBACK_RESUME_BUFFER)
//                .setTargetBufferBytes(-1)
//                .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl();
//        player = ExoPlayerFactory.newSimpleInstance(act, new DefaultTrackSelector(), loadControl);
        player = ExoPlayerFactory.newSimpleInstance(act);
        player.addListener(mMediaEventListener);

        playerView.setShutterBackgroundColor(Color.TRANSPARENT);
        playerView.setPlayer(player);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        player.prepare(source);
        player.setPlayWhenReady(true);

        //Use Media Session Connector from the EXT library to enable MediaSession Controls in PIP.
        MediaSessionCompat mediaSession = new MediaSessionCompat(getContext(), "TAG_MEDIA_SESSION");
        MediaSessionConnector mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setPlayer(player);
        mediaSession.setActive(true);

//        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
//        if (haveResumePosition) {
//            player.seekTo(player.getCurrentWindowIndex(), mResumePosition);
//        }
        player.seekTo(player.getCurrentWindowIndex(), videoPosition);
    }

    private void showQualityOptions(View view) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(act, view);

        for (int i = 0; i < videoClips.size(); ++i) {
            String optName = videoClips.get(i).height + "p";
            popup.getMenu().add(optName);
        }
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                YtFragmentedVideo clip = null;
                for (int i = 0; i < videoClips.size(); ++i) {
                    if (title.contains(String.valueOf(videoClips.get(i).height))) {
                        clip = videoClips.get(i);
                        break;
                    }
                }
                if (clip != null) {
                    playFromVideoExtract(clip);
                }
                Toast.makeText(act, "You change to : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show(); //showing popup menu
    }


    // utils method
    private void print(String msg) {
        Log.d(TAG, "...     ...    ...    ..." + msg);
    }
}
