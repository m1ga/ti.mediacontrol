package ti.mediacontrol;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;

import androidx.media.session.MediaButtonReceiver;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

public class MyMediaReceiver extends MediaButtonReceiver {
    private MediaListener mediaListener;

    public MyMediaReceiver() {
        super();
    }

    public MyMediaReceiver(MediaListener mediaListener) {
        super();
        this.mediaListener = mediaListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        MediaSessionCompat mSession = new MediaSessionCompat(TiApplication.getAppRootOrCurrentActivity(), "mediaSession");
        MediaButtonReceiver.handleIntent(mSession, intent);
        KeyEvent ev = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        Log.i("TiMediacontrolModule", "Processing media button: " + ev);
        if (mediaListener != null) {
            mediaListener.onMediaStateChanged(ev.getKeyCode());
        }
    }
}
