package dk.dtu.itdiplom.dturunner.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import dk.dtu.itdiplom.dturunner.Model.PointInfo;

/**
 * Created by jan on 12-11-2015.  http://developer.android.com/training/basics/data-storage/databases.html
 *                                  https://www.youtube.com/watch?v=p6UgUYSkDmE&feature=youtu.be&t=560
 */
public class DatabaseContract extends SQLiteOpenHelper {
//    public DatabaseContract(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DtuRunner.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String FLOAT_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_PointInfo_ENTRIES =
            "CREATE TABLE " + PointInfoDbContract.PointInfoDb.TABLE_NAME + " (" +

                    PointInfoDbContract.PointInfoDb._ID + " INTEGER PRIMARY KEY," +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_ENTRY_ID + INTEGER_TYPE + COMMA_SEP +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_LOEBS_ID + TEXT_TYPE + COMMA_SEP +      // skal være guid. derfor TEXT.
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_TIMESTAMP + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_LATITUDE + TEXT_TYPE + COMMA_SEP +      // Kunne være REAL ?
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_LONGITUDE + TEXT_TYPE + COMMA_SEP +     // Kunne være REAL ?
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_SPEED + TEXT_TYPE + COMMA_SEP +         // Kunne være REAL ?
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_DISTANCE + TEXT_TYPE + COMMA_SEP +      // Kunne være REAL ?
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_HEARTRATE + INTEGER_TYPE +                 // Kunne være INTEGER
                    " )";

    private static final String SQL_CREATE_LoebsAktivitet_ENTRIES =
            "CREATE TABLE " + PointInfoDbContract.LoebsAktivitetDb.TABLE_NAME + " (" +

                    PointInfoDbContract.LoebsAktivitetDb._ID + " INTEGER PRIMARY KEY," +
                    PointInfoDbContract.LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_LOEBSAKTIVITETS_ID + TEXT_TYPE + COMMA_SEP +    // guid ?
                    PointInfoDbContract.LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_PROGRAMTYPE + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_STARTTIDSPUNKT + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_NAVN + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_NOTE + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_PERSONID + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_EMAIL + TEXT_TYPE +
                    " )";




    public DatabaseContract(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            Log.d("jjSQLiteDatabase", "SQLiteDatabase creating in onCreate");
        db.execSQL(SQL_CREATE_LoebsAktivitet_ENTRIES);
        db.execSQL(SQL_CREATE_PointInfo_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
