package com.mitek.exomultiquality;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Toast;

import com.mitek.exomultiquality.models.YtFragmentedVideo;
import com.mitek.exomultiquality.models.YtStream;
import com.mitek.exomultiquality.player.HexoPlayer;

import org.schabi.newpipe.extractor.MediaFormat;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.ServiceList;
import org.schabi.newpipe.extractor.localization.Localization;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.VideoStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    static String VIDEO240_STREAM_URL = "https://r3---sn-8pxuuxa-q5qee.googlevideo.com/videoplayback?expire=1578347305&ei=yVYTXp2bOsWBowPf15KAAg&ip=171.249.157.184&id=o-AFntLJawbEcX_Un2KIXvuGNcdP52RDIcrCD9tM3bePhr&itag=133&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C278%2C394%2C395%2C396%2C397%2C398%2C399&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-8pxuuxa-q5qee%2Csn-npoe7nez&ms=au%2Crdu&mv=m&mvi=2&pl=21&initcwndbps=853750&mime=video%2Fmp4&gir=yes&clen=4211434&dur=215.506&lmt=1577471613799948&mt=1578325615&fvip=3&keepalive=yes&fexp=23842630&c=WEB&txp=5535432&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhALgcP46PUEUdskt3aVauexJpnLZ50Ubyxi3-m7Dpwf22AiEA-7F7dBQpi_Xl7HlZoA9DSDu4HENOSTS8jx54Szg4z7k%3D&sig=ALgxI2wwRAIgJm7pT8ggi0aoTw0mT6RlcO_bVbB3FQpmFPzg4FQC7w4CIB-jclo12gxrtzQlboR__ssaC28bZunHCiJ1Ub7LIWzy";
    static String VIDEO480_STREAM_URL = "https://r3---sn-8pxuuxa-q5qee.googlevideo.com/videoplayback?expire=1578347305&ei=yVYTXp2bOsWBowPf15KAAg&ip=171.249.157.184&id=o-AFntLJawbEcX_Un2KIXvuGNcdP52RDIcrCD9tM3bePhr&itag=135&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C278%2C394%2C395%2C396%2C397%2C398%2C399&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-8pxuuxa-q5qee%2Csn-npoe7nez&ms=au%2Crdu&mv=m&mvi=2&pl=21&initcwndbps=853750&mime=video%2Fmp4&gir=yes&clen=10880212&dur=215.506&lmt=1577471613803248&mt=1578325615&fvip=3&keepalive=yes&fexp=23842630&c=WEB&txp=5535432&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhALgcP46PUEUdskt3aVauexJpnLZ50Ubyxi3-m7Dpwf22AiEA-7F7dBQpi_Xl7HlZoA9DSDu4HENOSTS8jx54Szg4z7k%3D&sig=ALgxI2wwRQIgOlDbSFr0Jq4SeavIwHx7LQtxeYnnXfeJGVHIKCc79AICIQClblqBe4DRGpY6ltXyoDQUYNnsDO_zd4EZRD_oLb4G9g==";
    static String AUDIO_STREAM_URL = "https://r3---sn-8pxuuxa-q5qee.googlevideo.com/videoplayback?expire=1578347305&ei=yVYTXp2bOsWBowPf15KAAg&ip=171.249.157.184&id=o-AFntLJawbEcX_Un2KIXvuGNcdP52RDIcrCD9tM3bePhr&itag=140&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-8pxuuxa-q5qee%2Csn-npoe7nez&ms=au%2Crdu&mv=m&mvi=2&pl=21&initcwndbps=853750&mime=audio%2Fmp4&gir=yes&clen=3489536&dur=215.574&lmt=1577470951665372&mt=1578325615&fvip=3&keepalive=yes&fexp=23842630&c=WEB&txp=5531432&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhALgcP46PUEUdskt3aVauexJpnLZ50Ubyxi3-m7Dpwf22AiEA-7F7dBQpi_Xl7HlZoA9DSDu4HENOSTS8jx54Szg4z7k%3D&sig=ALgxI2wwRQIhAIM3Zq_E1HmGYT7rnruFeO8S1AkVNsw0ZR4LxrG9FIfNAiBERc4luzZuCx_Jlm6qsH83CsFRNY1qbrP32oPEHMfexQ==";
    static String LOCAL_VIDEO_PATH = "/storage/emulated/0/Download/vid.mp4";
    static String ARG_VIDEO_POSITION = "VideoActivity.POSITION";


    private List<YtFragmentedVideo> videoClips = new ArrayList<>();
    private HexoPlayer hexoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        print("create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hexoPlayer = (HexoPlayer) findViewById(R.id.hexoplayer);
        hexoPlayer.setActivity(this);
//        hexoPlayer.playFromFile(LOCAL_VIDEO_PATH);


        findViewById(R.id.btn240).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExtractVideo();

//                Pattern p = Pattern.compile("(\\d+)p.*");
//                Matcher m = p.matcher("720p1khjhh jkhjhjkh jkjkhk 860psdfsdf sf");
//                if(m.matches()) {
//                    System.out.println("The quantity is " + m.group(1));
//                }
            }
        });

        setupFloatingService();
//        fetchClips();

        doExtractVideo();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putLong(ARG_VIDEO_POSITION, hexoPlayer.getVideoPosition());

        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            long position = savedInstanceState.getLong(ARG_VIDEO_POSITION);
            hexoPlayer.setVideoPosition(position);
        }
        super.onRestoreInstanceState(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        print("resume");
        hexoPlayer.setReadyForClose(false);
        hexoPlayer.onPageResume();
    }

    @Override
    public void onPause() {
        print("pause");
        super.onPause();
        hexoPlayer.setReadyForClose(false);
        hexoPlayer.onPagePause();
    }

    @Override
    public void onStop() {
        print("release");
        super.onStop();
        hexoPlayer.setReadyForClose(true);
        if (!hexoPlayer.isInPipMode) { // will destroy player if not in PIP mode
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    && getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                }
            }
            hexoPlayer.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        hexoPlayer.enterPIPMode();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        hexoPlayer.onPipChange(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class PipExtractTask extends AsyncTask<String, Void, List<YtFragmentedVideo>> {

        long time;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            time = System.currentTimeMillis();
        }

        protected List<YtFragmentedVideo> doInBackground(String... youtubeId) {
            List<YtFragmentedVideo> clips = new ArrayList<>();
            try {
                String url = "https://www.youtube.com/watch?v=" + youtubeId[0];
                NewPipe.init(DownloaderImpl.init(null), new Localization("AR", "es"));
                StreamExtractor extractor = ServiceList.YouTube.getStreamExtractor(url);
                extractor.fetchPage();

                // Get best audio
                YtStream audioClip = null;
                for (AudioStream a : extractor.getAudioStreams()) {
                    if (a.getFormat() == MediaFormat.M4A) {
                        audioClip = new YtStream();
                        audioClip.url = a.getUrl();
                        audioClip.format = a.getFormat().suffix;
                        audioClip.height = String.valueOf(a.getAverageBitrate());
                        break;
                    }
                }

                if (audioClip == null) {
                    // get mix audio/video clip
                    return clips;
                }

                for (VideoStream v : extractor.getVideoOnlyStreams()) {
                    if (v.getFormat() == MediaFormat.MPEG_4) {
                        YtFragmentedVideo clip = new YtFragmentedVideo();
                        clip.videoFile = new YtStream(v.getUrl(), v.getResolution(), v.getFormat().suffix);
                        clip.audioFile = audioClip;
                        clip.height = Integer.parseInt(v.getResolution().substring(0, v.getResolution().length() - 1));
                        clips.add(clip);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return clips;
        }

        protected void onPostExecute(List<YtFragmentedVideo> clips) {
            videoClips = clips;


            Log.d(TAG, "extracted done " + clips.size());
            long delta = System.currentTimeMillis() - time;
            Log.d(TAG, "seconds: " + (delta/1000.0));

//            hexoPlayer.setVideos(clips);
//            hexoPlayer.play();
        }
    }

    private void doExtractVideo() {
        (new PipExtractTask()).execute("uR8Mrt1IpXg");
    }

    private void fetchClips() {
        print("fetch Clips");
        if (videoClips.size() == 0) {
            try {
                doExtractVideo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hexoPlayer.play();
        }
    }

    private void setupFloatingService() {
        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {


            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView();
        }
    }

    private void initializeView() {
        findViewById(R.id.btnFloating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    // utils method
    private void print(String msg) {
        Log.d(TAG, ">>>>>>>>>>>         " + msg);
    }

}
