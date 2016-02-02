package dk.dtu.itdiplom.dturunner.Utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dk.dtu.itdiplom.dturunner.Model.Entities.LoebsAktivitet;
import dk.dtu.itdiplom.dturunner.Model.PointInfo;

/**
 * Created by JanMøller on 17-11-2015.
 */
public class FileHelper {

    static final String LOGTAG = "jjFileHelper";

    public static File saveFileToExternalStorageLobsAktivitet(Context context, LoebsAktivitet loebsAktivitet)
    {
        String loebsData = makePointInfoString(loebsAktivitet.pointInfoList);
        return saveFileExternalStorage(context, loebsData);
    }
    
    private static String makePointInfoString(List<PointInfo> pointInfoList)
    {
        String separator = "\t";

        String line = "header\n";
        for (PointInfo pointInfo: pointInfoList) {
            line = "" + pointInfo.getTimestamp() + separator + pointInfo.getLatitude() + pointInfo.getLongitude() + pointInfo.getDistance() + pointInfo.getHeartRate();
            line+="\n";
        }
        return line;
    }



    /*
        Gem fil til external storage, så den kan sendes på mail.
     */
    private static File saveFileExternalStorage(Context context, String loebsInfoContent)
    {

        String filename = "dtuRunner_latest.txt";
        File fullFilename = new File(context.getExternalFilesDir(null), filename);

        try
        {
            // Creates a trace file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.

            Log.d(LOGTAG, "file write(getExternalFilesDir): " + context.getExternalFilesDir(null));


            if (!fullFilename.exists())
                fullFilename.createNewFile();
            // Adds a line to the trace file    - todo jan: lav om til ikke append, hvis det bliver samme fil
            BufferedWriter writer = new BufferedWriter(new FileWriter(fullFilename, true /*append*/));
            //writer.write("This is a test trace file.");
            writer.write("Header info skal være her 1...");
            writer.write(loebsInfoContent);
            writer.close();

            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
// note jan: dette kan bruges under debug...
//            MediaScannerConnection.scanFile(context,
//                    new String[]{fullFilename.toString()},
//                    null,
//                    null);

        }
        catch (IOException e)
        {
            Log.e(LOGTAG, "Fejl opstod ved skrivning af fil. " + e.getMessage() + "\n" + e.getStackTrace());
        }
        return fullFilename;
    }




    /*
        Gem fil til external storage, så den kan sendes på mail.
     */
    public static File saveFileExternalStorage2(Context context, LoebsAktivitet loebsAktivitet, boolean copyToFileWithDate)
    {
        String separator = "\t";

        String loebsDatoTime = loebsAktivitet.getLoebsDateFormattedToFileNaming();
        String navnAlias = loebsAktivitet.getNavnAlias();
        if(navnAlias == "")
            navnAlias = "noname";

        String filenameWithDate = "dtuRunner_" + navnAlias + "_" + loebsDatoTime + ".txt";
        // udvid til at lave 2 filer! hvor den ene får en dato.
        String filename = "dtuRunner_latest.txt";
        File fullFilename1 = new File(context.getExternalFilesDir(null), filename);
        File fullFilename2WithDate = new File(context.getExternalFilesDir(null), filenameWithDate);

        try
        {
            // Creates a trace file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.

            Log.d(LOGTAG, "file write(getExternalFilesDir): " + context.getExternalFilesDir(null));


            if (!fullFilename1.exists())
                fullFilename1.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fullFilename1, false /*append*/));

            writer.write("Header info skal være her 2...");
            writer.write("\n");
            writer.write("Navn/Alias: " + loebsAktivitet.getNavnAlias());
            writer.write(separator);
            writer.write("Gennnemsnitsfart: " + loebsAktivitet.getAverageSpeedFromStart() + " m/s.");
            writer.write(separator);
            writer.write("Distance i alt: " + loebsAktivitet.getTotalDistanceMeters() + " meter.");
            writer.write(separator);
            // todo jan 1/2-2016: ny tilføjelse. skal fjerne den ene når tetet af.
            writer.write("Distance i alt: " + loebsAktivitet.getTotalDistanceMetersFormatted(true));
            writer.write(separator);
            // todo jan 1/2-2016: ny tilføjelse
            writer.write("Varighed: " +  LocationUtils.displayTimerFormat(loebsAktivitet.getTimeMillisecondsSinceStart()));
            writer.write(separator);
            writer.write("" + loebsAktivitet.getTextHeader());
            writer.write(separator);
            writer.write("" + loebsAktivitet.getLoebsNote());
            writer.write("uuid: " + loebsAktivitet.getLoebsAktivitetUuid());
            writer.write("\n");
            writer.write("\n");
            writer.write("\n");

            String lineHeader = "header\n";
            writer.write(lineHeader);
            String headerTypes = String.format("%s%s%s%s%s%s%s%s%s%s",
                    "Tid(millisekunder)", separator, "Tid", separator, "Latitude", separator, "Longitude", separator, "Distance", separator, "Heartrate", separator);
            writer.write(headerTypes);
            writer.write("\n");

            String line = "";
            for (PointInfo pointInfo: loebsAktivitet.pointInfoList) {

                String dateTimeFormatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(pointInfo.getTimestamp()));
                line = "" + pointInfo.getTimestamp() + separator + dateTimeFormatted + separator + pointInfo.getLatitude() + separator + pointInfo.getLongitude()
                        + separator + pointInfo.getDistance() + separator + pointInfo.getHeartRate();
                line+="\n";
                writer.write(line);
            }

//            writer.write(line);
            writer.close();

        }
        catch (IOException e)
        {
            Log.e(LOGTAG, "Fejl opstod ved skrivning af fil. " + e.getMessage() + "\n" + e.getStackTrace());
        }

        if(copyToFileWithDate) {
            try {
                copy(fullFilename1, fullFilename2WithDate);
            }
            catch (IOException e) {
                Log.e(LOGTAG, "Fejl opstod ved kopiering af fil. Filnavne: '" + fullFilename1 + "', '" + fullFilename2WithDate + "'");
                Log.e(LOGTAG, "Fejl opstod ved kopiering af fil. " + e.getMessage() + "\n" + e.getStackTrace());
            }
        }
        return fullFilename1;
    }

    /*
        Kode taget herfra:
        http://stackoverflow.com/questions/9292954/how-to-make-a-copy-of-a-file-in-android
     */
    private static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
