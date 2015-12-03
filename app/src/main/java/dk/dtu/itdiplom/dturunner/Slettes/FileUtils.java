//package dk.dtu.itdiplom.dturunner.Slettes;
//
//import android.content.Context;
//
//import java.io.FileOutputStream;
//
///**
// * Created by JanMøller on 08-11-2015.
// */
//public class FileUtils {
//
//    /*
//        Gem fil. Stadig under test.
//
//
//     */
//    public static void saveLoebsDataToInternalStorage(Context context, String filename, String content)
//    {
//        FileOutputStream outputStream;
//
//        try {
//            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
//            outputStream.write(content.getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
