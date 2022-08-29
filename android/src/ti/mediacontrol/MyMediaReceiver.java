package ti.mediacontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;

import androidx.media.session.MediaButtonReceiver;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

public class MyMediaReceiver extends MediaButtonReceiver {
    public MyMediaReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        MediaSessionCompat mSession = new MediaSessionCompat(TiApplication.getAppRootOrCurrentActivity(), "mediaSession");
        MediaButtonReceiver.handleIntent(mSession, intent);
        Log.i("---", intent.getAction());
        KeyEvent ev = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        Log.i("---", "Processing media button: " + ev);
        //super.onReceive(context, intent);
        TiMediacontrolModule.fromReceiver();
    }
}
