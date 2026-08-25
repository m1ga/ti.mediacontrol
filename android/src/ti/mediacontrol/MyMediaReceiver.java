package ti.mediacontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;

import androidx.media.session.MediaButtonReceiver;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.common.Log;

public class MyMediaReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        KeyEvent ev = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (ev == null) {
            return;
        }
        Log.d("MyMediaReceiver", "Processing media button: " + ev);

        MediaSessionCompat session = TiMediacontrolModule.getActiveSession();
        if (session != null) {
            MediaButtonReceiver.handleIntent(session, intent);
        }

        TiMediacontrolModule module = TiMediacontrolModule.getInstance();
        if (module != null && ev.getAction() == KeyEvent.ACTION_DOWN) {
            KrollDict kd = new KrollDict();
            kd.put("keyCode", ev.getKeyCode());
            module.fireEvent("keyPress", kd);
        }
    }
}
