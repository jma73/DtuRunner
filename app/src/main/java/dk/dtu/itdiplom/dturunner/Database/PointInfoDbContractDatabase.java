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
public class PointInfoDbContractDatabase extends SQLiteOpenHelper {
//    public PointInfoDbContractDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DtuRunner.db";

    // todo jan 12/11: skal have lavet Tabeller
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_PointInfo_ENTRIES =
            "CREATE TABLE " + PointInfoDbContract.PointInfoDb.TABLE_NAME + " (" +

                    PointInfoDbContract.PointInfoDb._ID + " INTEGER PRIMARY KEY," +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_LOEBS_ID + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_TIMESTAMP + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_LATITUDE + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_LONGITUDE + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_SPEED + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_DISTANCE + TEXT_TYPE + COMMA_SEP +
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_HEARTRATE + TEXT_TYPE +
                    " )";

    public PointInfoDbContractDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            Log.d("jjSQLiteDatabase", "SQLiteDatabase creating in onCreate");
        db.execSQL(SQL_CREATE_PointInfo_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
