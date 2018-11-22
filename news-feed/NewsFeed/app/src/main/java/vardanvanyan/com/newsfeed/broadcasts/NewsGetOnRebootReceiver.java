package vardanvanyan.com.newsfeed.broadcasts;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import vardanvanyan.com.newsfeed.services.NewsNotificationService;

/**
 * Broadcast receiver for restart news notification service on device reboot.
 */
public class NewsGetOnRebootReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, NewsNotificationService.class));
    }
}
