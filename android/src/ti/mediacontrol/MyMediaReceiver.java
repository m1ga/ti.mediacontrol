package ti.mediacontrol;

import static android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media.session.MediaButtonReceiver;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

public class MyMediaReceiver extends MediaButtonReceiver {

    public MyMediaReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        MediaSessionCompat mSession = new MediaSessionCompat(TiApplication.getAppRootOrCurrentActivity(), "mediaSession");
        MediaButtonReceiver.handleIntent(mSession, intent);
        KeyEvent ev = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        Log.d("MyMediaReceiver", "Processing media button: " + ev);

        if (ev.getKeyCode() == KEYCODE_MEDIA_PLAY_PAUSE) {

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("keyPress")
                    .putExtra("status", "pause")
                    .putExtra("keyCode", ev.getKeyCode());
            LocalBroadcastManager.getInstance(TiApplication.getAppCurrentActivity()).sendBroadcast(broadcastIntent);
        }
    }
}