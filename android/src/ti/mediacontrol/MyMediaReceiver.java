package ti.mediacontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;

import androidx.media.session.MediaButtonReceiver;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

public class MyMediaReceiver extends MediaButtonReceiver {
    public MyMediaReceiver() {
        super();
    }
    private MediaListener mediaListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        MediaSessionCompat mSession = new MediaSessionCompat(TiApplication.getAppRootOrCurrentActivity(), "mediaSession");
        MediaButtonReceiver.handleIntent(mSession, intent);
        KeyEvent ev = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        Log.i("TiMediacontrolModule", "Processing media button: " + ev);
        mediaListener.onMediaStateChanged(ev.getKeyCode());
    }
}
