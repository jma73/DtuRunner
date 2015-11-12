package dk.dtu.itdiplom.dturunner.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import dk.dtu.itdiplom.dturunner.Model.PointInfo;

/**
 * Created by jan on 12-11-2015.
 */
public class PointInfoDbContract {


    public PointInfoDbContract()
    {}

    public abstract class PointInfoDb implements BaseColumns
    {
        public static final String TABLE_NAME = "PointInfo";

        //public static final String COLUMN_NAME_NULLABLE = "loebsaktivitetid";

        public static final String COLUMN_NAME_LOEBS_ID = "loebsaktivitetid";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_SPEED = "speed";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_HEARTRATE = "heartRate";


        //, double latitude, double longitude, double speed, double distance, int heartRate

//        public static final String COLUMN_NAME_TITLE = "title";
//        public static final String COLUMN_NAME_SUBTITLE = "subtitle";


    }

    // todo jan - tror nok at jeg skal flytte denne til sin egen klasse...
    public void insertPointData(PointInfo pointInfo, int loebsAktivitetId, Context context)
    {
        Log.d("JJdatabase", "insertPointData");
        //Log.d("JJdatabase", getfilesdir());


        // Gets the data repository in write mode
        PointInfoDbContractDatabase sqliteRepo = new PointInfoDbContractDatabase(context);
        SQLiteDatabase db = sqliteRepo.getWritableDatabase();

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
