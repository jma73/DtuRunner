package dk.dtu.itdiplom.dturunner.Model;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by JanMøller on 16-11-2015.
 */
public class FileHelperTest {

    public static void testSaveFile(Context context, String loebsInfoContent)
    {
        String filename = "dtuRunner.txt";      // todo bruge samme filnavn hvergang?
        String string = "Hello world!";
        string += "\n" + loebsInfoContent;
//        File dir = context.getFilesDir();
//        dir.getAbsolutePath()

        FileOutputStream outputStream;

        try {
            outputStream =  context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public byte[] testReadFile(Context context)
    {
        String filename = "dtuRunner.txt";      // todo bruge samme filnavn hvergang?

//        File dir = context.getFilesDir();
//        dir.getAbsolutePath()

        FileInputStream inputStream;

        try {
            inputStream =  context.openFileInput(filename);
            //return inputStream;

            byte[] t = readBytes(inputStream);
            return t;
//            byte[] buffer = new byte[1014];

//            inputStream.read(buffer);
//            inputStream.close();
//            return buffer;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return new byte[0];
    }

    // http://stackoverflow.com/questions/2436385/android-getting-from-a-uri-to-an-inputstream-to-a-byte-array
    private byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }


    public static File testSaveFileExternalStorage(Context context, String loebsInfoContent)
    {

        String filename = "dtuRunner.txt";
//        File file2 = new File(context.getFilesDir(), filename);
//        File file = new File(context.getExternalFilesDir(null), filename);

        File traceFile = new File(context.getExternalFilesDir(null), filename);
        try
        {
            // Creates a trace file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.

            Log.d("jj", "file write: " + context.getExternalFilesDir(null));

            if (!traceFile.exists())
                traceFile.createNewFile();
            // Adds a line to the trace file
            BufferedWriter writer = new BufferedWriter(new FileWriter(traceFile, true /*append*/));
            writer.write("This is a test trace file.");
            writer.close();
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(context,
                    new String[]{traceFile.toString()},
                    null,
                    null);

        }
        catch (IOException e)
        {
            Log.e("jj.FileTest", "Unable to write to the TraceFile.txt file.");
        }
        return traceFile;
    }









//        try{
//            boolean t = file.createNewFile();
//            file.
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        String filename = "dtuRunner.txt";      // todo bruge samme filnavn hvergang?
//        String string = "Hello world!";
//        string += "\n" + loebsInfoContent;
////        File dir = context.getFilesDir();
////        dir.getAbsolutePath()
//
//        FileOutputStream outputStream;
//
//        try {
//            outputStream =  context.openFileOutput(filename, Context.MODE_PRIVATE);
//            outputStream.write(string.getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }

}
