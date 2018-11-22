package vardanvanyan.com.newsfeed;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import vardanvanyan.com.newsfeed.constants.Constants;
import vardanvanyan.com.newsfeed.services.NewsNotificationService;

public class NewsFeedApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        registerActivityLifecycleCallbacks(this);
        intent = new Intent(this, NewsNotificationService.class);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // Required implementation
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            // App enters foreground
            stopService(intent);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        // Required implementation
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // Required implementation
    }

    @Override
    public void onActivityStopped(Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App enters background
            startService(intent);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // Required implementation
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // Required implementation
    }

     /* Helper Methods */

    /**
     * Creates notification chanel for receiving updated news notification via already opened chanel
     * This is require only for Android devices higher than Android.O
     */
     private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final CharSequence name = getString(R.string.channel_name);
            final String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            final NotificationChannel channel =
                    new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            final NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
