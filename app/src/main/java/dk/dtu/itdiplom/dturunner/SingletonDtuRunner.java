package dk.dtu.itdiplom.dturunner;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import dk.dtu.itdiplom.dturunner.Model.Entities.LoebsStatus;
import dk.dtu.itdiplom.dturunner.Utils.BuildInfo;

/**
 * Created by jan on 19-11-2015.
 */
public class SingletonDtuRunner extends Application {
    private static SingletonDtuRunner ourInstance = new SingletonDtuRunner();

    public static String fragmentLoebTag = "FragmentLoeb";

    public static String buildDate;
    public static LoebsStatus loebsStatus = new LoebsStatus();

    public static SingletonDtuRunner getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        Log.d("SingletonDtuRunner", "onCreate() kaldt");
        super.onCreate();
        ourInstance = this;
        buildDate = BuildInfo.GetBuildDate(getApplicationContext());
    }


    // todo jan - til anvendelse fremover. taget fra
    public static final boolean EMULATOR = Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator"); // false;
    public static boolean erUnderUdviklingFlag = true;

}
