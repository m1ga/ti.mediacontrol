package ti.mediacontrol;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

import java.util.List;

public class MyService extends MediaBrowserServiceCompat {

    private final MediaSessionCompat.Callback mMediaSessionCallback = new MediaSessionCompat.Callback() {

        @Override
        public void onPlay() {
            Log.d("MediaService", "service: play");
            super.onPlay();
        }

        @Override
        public void onPause() {
            Log.d("MediaService", "service: pause");
            super.onPause();
        }
    };
    private MediaSessionCompat mMediaSessionCompat;

    @Override
    @SuppressLint("NewApi")
    public void onCreate() {
        super.onCreate();
        Log.d("MediaService", "oncreate service");
        ComponentName mediaButtonReceiver = new ComponentName(TiApplication.getAppRootOrCurrentActivity(), MyMediaReceiver.class);
        mMediaSessionCompat = new MediaSessionCompat(TiApplication.getAppRootOrCurrentActivity(), "Tag", mediaButtonReceiver, null);

        mMediaSessionCompat.setCallback(mMediaSessionCallback);
        mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MediaService", "start command");
        MyMediaReceiver.handleIntent(mMediaSessionCompat, intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
