package dk.dtu.itdiplom.dturunner;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LoebsAktivitetService extends Service {

    final String TAG = getClass().getName();

    public LoebsAktivitetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG, "in onBind");

        return null;
    }

    @Override
    public void onCreate() {

        Toast.makeText(this, TAG + " onCreate", Toast.LENGTH_LONG).show();
        Log.d(TAG, "in onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "in onDestroy");
        Toast.makeText(this, TAG + " onDestroy", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String tekst = " onStartCommand("+ flags + " " + startId;
        Log.d(TAG, "in onStartCommand : " + tekst);


        if (intent!=null && intent.getExtras()!=null) tekst+="\n"+intent.getExtras().get("nøgle");
        Toast.makeText(this, TAG + tekst, Toast.LENGTH_LONG).show();
        return START_STICKY; // eller START_REDELIVER_INTENT hvis intent skal overleve procesgenstart
    }
}
