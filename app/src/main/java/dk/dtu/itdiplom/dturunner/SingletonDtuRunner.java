package dk.dtu.itdiplom.dturunner;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import dk.dtu.itdiplom.dturunner.Model.Entities.LoebsStatus;
import dk.dtu.itdiplom.dturunner.Utils.BuildInfo;

/**
 * Created by jan on 19-11-2015.
 */
public class SingletonDtuRunner extends Application {
    private static SingletonDtuRunner ourInstance = new SingletonDtuRunner();

    public static String buildDate;
    //public static boolean isLoebsAktivitetStartet;
    public static LoebsStatus loebsStatus;
    public GoogleApiClient googleApiClient;

    public static SingletonDtuRunner getInstance() {
        return ourInstance;
    }

//    private SingletonDtuRunner() {
//    }

    @Override
    public void onCreate() {
        Log.d("SingletonDtuRunner", "onCreate() kaldt");
        super.onCreate();
        ourInstance = this;

        buildDate = BuildInfo.GetBuildDate(getApplicationContext());
    }

    public static final boolean EMULATOR = Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator"); // false;
    public static boolean udvikling = true;

    // protected GoogleApiClient googleApiClient;


}
