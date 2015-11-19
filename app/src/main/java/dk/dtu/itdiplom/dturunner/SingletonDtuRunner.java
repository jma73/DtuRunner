package dk.dtu.itdiplom.dturunner;

import android.app.Application;
import android.os.Build;
import android.util.Log;

/**
 * Created by jan on 19-11-2015.
 */
public class SingletonDtuRunner extends Application {
    private static SingletonDtuRunner ourInstance = new SingletonDtuRunner();

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
    }

    public static final boolean EMULATOR = Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator"); // false;
    public static boolean udvikling = true;
}
