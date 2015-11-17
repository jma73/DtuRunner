package dk.dtu.itdiplom.dturunner.Model;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by JanMøller on 17-11-2015.
 */
public class FileHelper {



    /*
        Gem fil til external storage, så den kan sendes på mail.
     */
    public static File saveFileExternalStorage(Context context, String loebsInfoContent)
    {

        String filename = "dtuRunner_latest.txt";
        File fullFilename = new File(context.getExternalFilesDir(null), filename);

        try
        {
            // Creates a trace file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.

            Log.d("jj", "file write: " + context.getExternalFilesDir(null));

            if (!fullFilename.exists())
                fullFilename.createNewFile();
            // Adds a line to the trace file    - todo jan: lav om til ikke append, hvis det bliver samme fil
            BufferedWriter writer = new BufferedWriter(new FileWriter(fullFilename, true /*append*/));
            writer.write("This is a test trace file.");
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
            Log.e("jj.FileTest", "Fejl opstod ved skrivning af fil. " + e.getMessage() + "\n" + e.getStackTrace());
        }
        return fullFilename;
    }
}
