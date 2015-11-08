package dk.dtu.itdiplom.dturunner.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by JanMøller on 08-11-2015.
 */
public class Database {

    // todo jan - opret database.
    //      der skal gemmes løbsdata. samt metadata om hvert løb.


    public static void createDatabase(Context context)
    {
        // Oprettelse af database
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir() + "/database.db", null);

        // Oprette tabel - foregår via SQL
        // db.execSQL("DROP TABLE IF EXISTS LoebsInfo;");
        db.execSQL("CREATE TABLE LoebsInfo (loebsId INTEGER PRIMARY KEY, loebsNavn TEXT NOT NULL, loebsDatetime INTEGER);");


        // https://www.sqlite.org/datatype3.html
        // skal datatime være numeric??? eller TEXT
        // db.execSQL("CREATE TABLE LoebsData (_id INTEGER PRIMARY KEY, loebsId Integer, latitude REAL NOT NULL, longitude REAL NOT NULL, datetime DATETIME);");
        db.execSQL("CREATE TABLE LoebsData (_id INTEGER PRIMARY KEY, loebsId Integer, latitude REAL NOT NULL, longitude REAL NOT NULL, datetime TEXT);");

    }


}
