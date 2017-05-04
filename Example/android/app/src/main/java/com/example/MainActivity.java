package com.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "example";
    }

    @Override
    public void onBackPressed() {
        if(isFullScreenOn){
            sendHardwareBackButtonPressedEvent();
            isFullScreenOn = false;
        } else {
            super.onBackPressed();
        }
    }

    private void sendHardwareBackButtonPressedEvent() {
        Intent intent = new Intent("hardware_back_button_pressed");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mReceiverYoutubeFullScreenButtonPressed);
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mReceiverYoutubeFullScreenButtonPressed, new IntentFilter("youtube_full_screen_button_pressed"));
        super.onResume();
    }

    private boolean isFullScreenOn = true;

    private BroadcastReceiver mReceiverYoutubeFullScreenButtonPressed = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isFullScreenOn = true;
            android.util.Log.d(TAG, "isFullScreenOn");
        }
    };

}
