/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2018 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.mediacontrol;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media.session.MediaButtonReceiver;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiDrawableReference;


@RequiresApi(api = Build.VERSION_CODES.O)

@Kroll.module(name = "TiMediacontrol", id = "ti.mediacontrol")
public class TiMediacontrolModule extends KrollModule {

    @Kroll.constant
    static final int PAUSE = 0;
    @Kroll.constant
    static final int PLAY = 1;
    @Kroll.constant
    static final int PREVIOUS = 2;
    @Kroll.constant
    static final int NEXT = 3;
    // Standard Debugging variables
    private static final String LCAT = "TiMediacontrolModule";
    private static final boolean DBG = TiConfig.LOGD;
    private final IntentFilter mIntentFilter;
    protected MediaSessionManager mManager;
    protected MediaSessionCompat mSession;
    protected MediaController mController;
    NotificationCompat.Action playPauseAction;
    NotificationCompat.Action playAction;
    int NOTIFICATION_ID = 999;
    String CHANNEL_ID = "TiMediaControls";
    Context context;
    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "mediaControls", NotificationManager.IMPORTANCE_DEFAULT);
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
    private final MyMediaReceiver myMediaReceiver;

    public TiMediacontrolModule() {
        super();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("keyPress");
        myMediaReceiver = new MyMediaReceiver();
        LocalBroadcastManager.getInstance(TiApplication.getAppRootOrCurrentActivity()).registerReceiver(myMediaReceiver, mIntentFilter);
    }

    public class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            KrollDict kd = new KrollDict();
            Log.i(LCAT, "LOCAL RECEIVER");
            String action = intent.getAction();
            if (action.equals("keyPress")) {
                kd.put("status", intent.getStringExtra("status"));
                kd.put("keyCode", intent.getIntExtra("keyCode", -1));
                fireEvent("changeStatus", kd);
            }
        }
    }

    @Kroll.onAppCreate
    public static void onAppCreate(TiApplication app) {

    }

    @Kroll.setProperty
    public void setTitle(String value) {
        if (notificationBuilder != null && notificationManager != null) {
            notificationBuilder.setContentTitle(value);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    @Kroll.setProperty
    public void setText(String value) {
        if (notificationBuilder != null && notificationManager != null) {
            notificationBuilder.setContentText(value);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    @Kroll.setProperty
    public void setBackgroundImage(String value) {
        TiDrawableReference source = TiDrawableReference.fromObject(this, value);
        if (!source.isTypeNull()) {
            notificationBuilder.setLargeIcon(source.getBitmap());
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    @Kroll.method
    public void updateInfo(KrollDict options) {
        if (notificationBuilder != null && notificationManager != null) {
            notificationBuilder.setContentTitle(options.getString(("title")));
            notificationBuilder.setContentText(options.getString(("text")));
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    @Kroll.method
    public void close() {
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    @Kroll.method
    public void createPlayer(KrollDict options) {
        if (mSession != null) {
            mSession.release();
        }
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        context = TiApplication.getAppCurrentActivity();
        mSession = new MediaSessionCompat(context, CHANNEL_ID);

        long actions = PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE
                | PlaybackStateCompat.ACTION_PLAY_PAUSE;

        if (options.containsKeyAndNotNull("showNext")) {
            actions |= PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        }
        if (options.containsKeyAndNotNull("showPrevious")) {
            actions |= PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
        }
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder().setActions(actions);

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(context, new MyMediaReceiver().getClass());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, mediaButtonIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        mSession.setMediaButtonReceiver(pendingIntent);
        mSession.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0, 0.0f).build());
        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                Log.i("---", "mSession: clicked play");
                super.onPlay();
                KrollDict kd = new KrollDict();
                kd.put("status", PLAY);
                fireEvent("changeStatus", kd);
                mSession.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0, 0.0f).build());

                notificationBuilder.addAction(playAction);
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            }

            @Override
            public void onPause() {
                Log.i("---", "mSession: clicked pause");
                super.onPause();
                KrollDict kd = new KrollDict();
                kd.put("status", PAUSE);
                fireEvent("changeStatus", kd);
                mSession.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0, 0.0f).build());
                notificationBuilder.addAction(playPauseAction);
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                KrollDict kd = new KrollDict();
                kd.put("status", NEXT);
                fireEvent("changeStatus", kd);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                KrollDict kd = new KrollDict();
                kd.put("status", PREVIOUS);
                fireEvent("changeStatus", kd);
            }

            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                return super.onMediaButtonEvent(mediaButtonEvent);
            }
        });
        mSession.setActive(true);

        notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);

        playPauseAction = new NotificationCompat.Action(
                R.drawable.ic_pause, "Pause",
                MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PLAY_PAUSE)
        );
        playAction = new NotificationCompat.Action(
                R.drawable.ic_play_arrow, "Play",
                MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PLAY)
        );

        // PendingIntent contentPendingIntent = PendingIntent.getActivity(context,
        //      0, new Intent(TiApplication.getAppRootOrCurrentActivity().getIntent()),
        //      PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        // );

        MediaControllerCompat controller = mSession.getController();
        notificationBuilder.setContentTitle(options.getString("title"))
                .setContentText(options.getString("text"))
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentIntent(controller.getSessionActivity())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(playPauseAction)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mSession.getSessionToken())
                        .setShowActionsInCompactView(0)
                );

        if (options.containsKeyAndNotNull("color")) {
            notificationBuilder.setColorized(true);
            notificationBuilder.setColor(TiConvert.toColor(options.get("color"), context));
        }
        if (options.containsKeyAndNotNull("icon")) {
            notificationBuilder.setSmallIcon(options.getInt("icon"));
        }

        if (options.containsKeyAndNotNull("backgroundImage")) {
            TiDrawableReference source = TiDrawableReference.fromObject(this, options.get("backgroundImage"));
            if (!source.isTypeNull()) {
                notificationBuilder.setLargeIcon(source.getBitmap());
            }
        }

        notificationChannel.setSound(null, null);
        notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
