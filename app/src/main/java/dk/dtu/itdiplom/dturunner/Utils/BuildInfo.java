package dk.dtu.itdiplom.dturunner.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by JanMøller on 01-11-2015.
 */
public class BuildInfo {

    /*
    * Prøver at hente dato fra manifest filen.
    * */
    public static String GetBuildDate(Context context)
    {
        try
        {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            ZipFile zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");
            long time = ze.getTime();
            SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
            formatter.setTimeZone(TimeZone.getTimeZone("gmt"));
            String s = formatter.format(new java.util.Date(time));
            zf.close();
            return s;
        }catch(Exception e)
        { }

        return "N/A";
    }

}
