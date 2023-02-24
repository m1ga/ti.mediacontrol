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
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.util.TiUIHelper;
import org.appcelerator.titanium.view.TiDrawableReference;


@RequiresApi(api = Build.VERSION_CODES.O)

@Kroll.module(name = "TiMediacontrol", id = "ti.mediacontrol")
public class TiMediacontrolModule extends KrollModule {

    // Standard Debugging variables
    private static final String LCAT = "TiMediacontrolModule";
    private static final boolean DBG = TiConfig.LOGD;
    protected MediaSessionManager mManager;
    protected MediaSessionCompat mSession;
    protected MediaController mController;
    NotificationCompat.Action playPauseAction;

    @Kroll.constant static final int PAUSE = 0;
    @Kroll.constant static final int PLAY = 1;
    @Kroll.constant static final int PREVIOUS = 2;
    @Kroll.constant static final int NEXT = 3;
    int NOTIFICATION_ID = 999;
    String CHANNEL_ID = "media";
    Context context;
    NotificationChannel notificationChannel = new NotificationChannel("media", "miscellaneous", NotificationManager.IMPORTANCE_DEFAULT);
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    public TiMediacontrolModule() {
        super();
    }

    @Kroll.onAppCreate
    public static void onAppCreate(TiApplication app) {
    }

    public static void fromReceiver() {

    }

    @Kroll.setProperty
    public void setTitle(String value) {
        notificationBuilder.setContentTitle(value);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Kroll.setProperty
    public void setText(String value) {
        notificationBuilder.setContentText(value);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
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
        notificationBuilder.setContentTitle(options.getString(("title")));
        notificationBuilder.setContentText(options.getString(("text")));
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Kroll.method
    public void close() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Kroll.method
    public void createPlayer(KrollDict options) {
        context = TiApplication.getAppRootOrCurrentActivity();
        mSession = new MediaSessionCompat(context, "media");

        long actions = PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE
                | PlaybackStateCompat.ACTION_PLAY_PAUSE;

        if (options.containsKeyAndNotNull("showNext")){
            actions |= PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        }
        if (options.containsKeyAndNotNull("showPrevious")){
            actions |= PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
        }
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder().setActions(actions);

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(context, MediaButtonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, mediaButtonIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        mSession.setMediaButtonReceiver(pendingIntent);
        mSession.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0, 0.0f).build());
        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                KrollDict kd = new KrollDict();
                kd.put("status", PLAY);
                fireEvent("changeStatus", kd);
                mSession.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0, 0.0f).build());
            }

            @Override
            public void onPause() {
                super.onPause();
                KrollDict kd = new KrollDict();
                kd.put("status", PAUSE);
                fireEvent("changeStatus", kd);
                mSession.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0, 0.0f).build());
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
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mSession.getSessionToken())
                        .setShowActionsInCompactView(0));

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
        notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

}