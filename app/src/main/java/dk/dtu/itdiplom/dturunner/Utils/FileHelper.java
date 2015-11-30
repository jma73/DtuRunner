package dk.dtu.itdiplom.dturunner.Utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public static File saveFileExternalStorage2(Context context, LoebsAktivitet loebsAktivitet)
    {

        String separator = "\t";

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
            BufferedWriter writer = new BufferedWriter(new FileWriter(fullFilename, false /*append*/));

            writer.write("Header info skal være her 2...");
            writer.write("Navn/Alias: " + loebsAktivitet.getNavnAlias());
            writer.write("Gennnemsnitsfart: " + loebsAktivitet.getAverageSpeedFromStart() + " m/s.");
            writer.write("Distance i alt: " + loebsAktivitet.getTotalDistanceMeters() + " meter.");
            writer.write("" + loebsAktivitet.getTextHeader());
            writer.write("" + loebsAktivitet.getLoebsNote());
            writer.write("uuid: " + loebsAktivitet.getLoebsAktivitetUuid());

            String lineHeader = "header\n";
            writer.write(lineHeader);
            String headerTypes = String.format("%s%s%s%s%s%s%s%s%s%s",
                    "Tid", separator, "Latitude", separator, "Longitude", separator, "Distance", separator, "Heartrate", separator);
            writer.write(headerTypes);

            String line = "";
            for (PointInfo pointInfo: loebsAktivitet.pointInfoList) {
                line = "" + pointInfo.getTimestamp() + separator + pointInfo.getLatitude() + separator + pointInfo.getLongitude()
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
        return fullFilename;
    }

}
