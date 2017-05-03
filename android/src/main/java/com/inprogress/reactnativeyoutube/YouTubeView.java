package com.inprogress.reactnativeyoutube;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.netcetera.reactnative.nativelib.R;

import android.util.Log;


public class YouTubeView extends RelativeLayout {

    public static final String TAG = "YouTubeView";

    YouTubePlayerController youtubeController;
    private YouTubePlayerFragment youTubePlayerFragment;
    public static String youtube_key;

    private Activity parentActivity;

    public YouTubeView(ReactContext context) {
        super(context);
        parentActivity = context.getCurrentActivity();
        init();
    }

    private ReactContext getReactContext() {
        return (ReactContext) getContext();
    }

    //start
//View has onDetachedFromWindow() when it is removed from the screen,
// but this is not related to it being destroyed -- it could be attached again, which will call onAttachedToWindow().
//end
    public void init() {
        android.util.Log.d(TAG, "init");
        inflate(getContext(), R.layout.youtube_layout, this);
        fragmentManager = getReactContext().getCurrentActivity().getFragmentManager();

        youTubePlayerFragment = (YouTubePlayerFragment) fragmentManager
                .findFragmentById(R.id.youtubeplayerfragment);
        youtubeController = new YouTubePlayerController(YouTubeView.this);
    }

    private FragmentManager fragmentManager;
    //start
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "mMessageReceiver.onReceive");
            youtubeController.closeFullScreen();
            //String message = intent.getStringExtra("message");
            //Log.d("receiver", "Got message: " + message);
        }
    };

    //end
    @Override
    protected void onAttachedToWindow() {
        LocalBroadcastManager.getInstance(parentActivity).registerReceiver(
                mMessageReceiver, new IntentFilter("back_pressed_event"));
        super.onAttachedToWindow();

    }
    @Override
    protected void onDetachedFromWindow() {
        try {
            LocalBroadcastManager.getInstance(parentActivity).unregisterReceiver(
                    mMessageReceiver);
            youTubePlayerFragment = (YouTubePlayerFragment)
                    fragmentManager.findFragmentById(R.id.youtubeplayerfragment);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.remove(youTubePlayerFragment);
            ft.commit();
        } catch (Exception e) {
            Log.e(TAG, "Exception thrown in onDetachedFromWindow method...", e);
        }
        getReactContext()
                .getCurrentActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onDetachedFromWindow();
    }

    public void seekTo(int second) {
        youtubeController.seekTo(second);
    }


    public void playerViewDidBecomeReady() {
        WritableMap event = Arguments.createMap();
        ReactContext reactContext = (ReactContext) getContext();
        event.putInt("target", getId());
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "ready", event);
    }


    public void didChangeToState(String param) {
        WritableMap event = Arguments.createMap();
        event.putString("state", param);
        event.putInt("target", getId());
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "state", event);
    }


    public void didChangeToQuality(String param) {
        WritableMap event = Arguments.createMap();
        event.putString("quality", param);
        event.putInt("target", getId());
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "quality", event);
    }


    public void didPlayTime(String current, String duration) {
        WritableMap event = Arguments.createMap();
        event.putString("currentTime", current);
        event.putString("duration", duration);
        event.putInt("target", getId());
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "progress", event);
    }


    public void receivedError(String param) {
        WritableMap event = Arguments.createMap();
        ReactContext reactContext = (ReactContext) getContext();
        event.putString("error", param);
        event.putInt("target", getId());
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "error", event);
    }


    public void setVideoId(String str) {
        youtubeController.setVideoId(str);
    }

    public void setInline(Boolean bool) {
        youtubeController.setPlayInline(bool);
    }

    public void setShowInfo(Boolean bool) {
        youtubeController.setShowInfo(bool);
    }

    public void setModestbranding(Boolean bool) {
        youtubeController.setModestBranding(bool);
    }

    public void setControls(Integer nb) {
        youtubeController.setControls(nb);
    }

    public void setPlay(Boolean bool) {
        youtubeController.setPlay(bool);
    }

    public void setHidden(Boolean bool) {
        youtubeController.setHidden(bool);
    }

    public void setApiKey(String apiKey) {
        youtube_key = apiKey;
        youTubePlayerFragment.initialize(youtube_key, youtubeController);
    }

    public void setLoop(Boolean loop) {
        youtubeController.setLoop(loop);
    }

    public void setRelated(Boolean related) {
        youtubeController.setRelated(related);
    }

    public void setFullscreen(Boolean bool) {
        youtubeController.setFullscreen(bool);
    }

    //VAMP-805 - React-native-youtube: Sometimes UI controls do not show on Android
    //must use a real device, on the emulator I can not play the video (and can not reproduce the bug)
    //open NZI Companion App with js file modified
    //config/state/app-android-production.js
    //you can change videoid here
    //"content": {
    //    "videoId": "lJ5Ab-xATrE"
    //}
    //steps to reproduce the bug
    //1. open the app
    //2. press full screen
    //3. go back and see that the controller is missing
    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }

    private final Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };

    //new onbackpressed try

}
