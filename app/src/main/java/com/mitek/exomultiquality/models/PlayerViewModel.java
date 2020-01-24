package com.mitek.exomultiquality.models;

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.mitek.exomultiquality.R;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;


public class PlayerViewModel extends ViewModel {

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableField<String> artist = new ObservableField<>();

    public final ObservableField<String> timeStart = new ObservableField<>();

    public final ObservableField<String> timeEnd = new ObservableField<>();

    public final ObservableField<String> coverImg = new ObservableField<>();


    public final ObservableField<RotateAnimation> animationImage = new ObservableField<>();
    public final ObservableField<Drawable> placeHolder = new ObservableField<>();

    public final ObservableInt maxSeekDuration = new ObservableInt();

    public final ObservableInt currentSeekPosition = new ObservableInt();

    public final ObservableBoolean isPlaying = new ObservableBoolean();

    public final ObservableField<MaterialDrawableBuilder.IconValue> playModeIcon = new ObservableField<>();

    {
//        title.set(CoreApplication.getInstance().getString(R.string.app_name));
//        artist.set(CoreApplication.getInstance().getString(R.string.app_name));
//        placeHolder.set(CoreApplication.getInstance().getResources().getDrawable(R.drawable.ic_empty_music2));
//
//        if (PlayerManager.getInstance().getRepeatMode() == PlayingInfoManager.RepeatMode.LIST_LOOP) {
//            playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT);
//        } else if (PlayerManager.getInstance().getRepeatMode() == PlayingInfoManager.RepeatMode.ONE_LOOP) {
//            playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
//        } else {
//            playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_OFF);
//        }
    }
}
