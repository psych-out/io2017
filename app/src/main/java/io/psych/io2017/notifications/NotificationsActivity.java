package io.psych.io2017.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import io.psych.io2017.MainActivity;
import io.psych.io2017.R;

public class NotificationsActivity extends Activity {

    private static final String TAG = "NotificationsActivity";

    private static final int HEADLINER_NOTIFICATION_ID = 1;
    private static final int EMERGENCY_NOTIFICATION_ID = 2;
    private static final int NORMAL_NOTIFICATION_ID = 3;
    private static final int EXTRA_NOTIFICATION_ID = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initializeNotificationChannels();

        findViewById(R.id.button_send_headliner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendHeadlinerNotification();
            }
        });

        findViewById(R.id.button_send_emergency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmergencyNotification();
            }
        });

        findViewById(R.id.button_send_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNormalNotification();
            }
        });

        findViewById(R.id.button_send_extra).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendExtraNotification();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initializeNotificationChannels() {
        // Initialization only effects the system the first time its run for an installed app.  You
        // can only change certain aspects of a channel after you have created them.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createEmergencyChannel(notificationManager);
        createNormalChannel(notificationManager);
        createExtrasChannel(notificationManager);
    }

    private void createEmergencyChannel(@NonNull NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(
                "emergency",
                "Emergency Messages",
                NotificationManager.IMPORTANCE_MAX
        );
        channel.setDescription("Source of emergency messages.");
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        notificationManager.createNotificationChannel(channel);
    }

    private void createNormalChannel(@NonNull NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(
                "normal",
                "Normal Messages",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Source of normal messages.");
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(channel);
    }

    private void createExtrasChannel(@NonNull NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(
                "extras",
                "Extra Messages",
                NotificationManager.IMPORTANCE_MIN
        );
        channel.setDescription("Source of extra messages.");
        notificationManager.createNotificationChannel(channel);
    }

    private void sendHeadlinerNotification() {
        // The only way to show a fully colorized notification is to show a notification for a
        // foreground Service.
        startService(new Intent(this, ForegroundNotificationService.class));
    }

    private void sendEmergencyNotification() {
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Emergency")
                .setContentText("This is an emergency notification.")
                .setColorized(true)
                .setColor(Color.RED)
                .setChannel("emergency")
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(EMERGENCY_NOTIFICATION_ID, notification);
    }

    private void sendNormalNotification() {
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Normal")
                .setContentText("This is a normal notification.")
                .setChannel("normal")
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NORMAL_NOTIFICATION_ID, notification);
    }

    private void sendExtraNotification() {
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Extra")
                .setContentText("This is an extra notification.")
                .setChannel("extra")
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(EXTRA_NOTIFICATION_ID, notification);
    }

    public static class ForegroundNotificationService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // The only way to show a fully colorized notification is to show a notification for a
            // foreground Service. That's what we're doing here.
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.psych_green_2);
            Palette palette = Palette.from(icon).generate();

            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Headliner")
                    .setContentText("This is a headliner notification.")
                    .setLargeIcon(icon)
                    .setContentIntent(pendingIntent)
                    .setTicker("This is the ticker.")
                    .setOngoing(true)
                    .setColorized(true)
                    .setColor(palette.getLightVibrantColor(Color.BLACK))
                    .setStyle(new Notification.MediaStyle())
                    .setChannel("emergency")
                    .build();
            startForeground(HEADLINER_NOTIFICATION_ID, notification);

            return START_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
