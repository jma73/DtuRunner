package dk.dtu.itdiplom.dturunner.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dk.dtu.itdiplom.dturunner.Model.PointInfo;

/**
 * Created by jan on 12-11-2015.
 */
public class Database extends SQLiteOpenHelper {
//    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
                    PointInfoDbContract.PointInfoDb.COLUMN_NAME_HEARTRATE + TEXT_TYPE + COMMA_SEP +
            " )";

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PointInfo_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    
    public void insertPointData(PointInfo pointInfo, int loebsAktivitetId)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PointInfoDbContract.PointInfoDb.COLUMN_NAME_ENTRY_ID, "1");  // ????
        values.put(PointInfoDbContract.PointInfoDb.COLUMN_NAME_LOEBS_ID, loebsAktivitetId);
        values.put(PointInfoDbContract.PointInfoDb.COLUMN_NAME_LATITUDE, pointInfo.getLatitude());
        values.put(PointInfoDbContract.PointInfoDb.COLUMN_NAME_LONGITUDE, pointInfo.getLongitude());
        values.put(PointInfoDbContract.PointInfoDb.COLUMN_NAME_DISTANCE, pointInfo.getDistance());
        values.put(PointInfoDbContract.PointInfoDb.COLUMN_NAME_SPEED, pointInfo.getSpeed());
        values.put(PointInfoDbContract.PointInfoDb.COLUMN_NAME_HEARTRATE, pointInfo.getHeartRate());
        values.put(PointInfoDbContract.PointInfoDb.COLUMN_NAME_TIMESTAMP, pointInfo.getTimestamp());

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                PointInfoDbContract.PointInfoDb.TABLE_NAME,
                null,
//                PointInfoDbContract.PointInfoDb.COLUMN_NAME_NULLABLE,
                values);
        
        
    }
    
}
