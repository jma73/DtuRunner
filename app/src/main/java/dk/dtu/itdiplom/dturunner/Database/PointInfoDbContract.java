package dk.dtu.itdiplom.dturunner.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.format.Time;
import android.util.Log;

import java.sql.Date;
import java.util.UUID;

import dk.dtu.itdiplom.dturunner.Model.LoebsAktivitet;
import dk.dtu.itdiplom.dturunner.Model.PointInfo;

/**
 * Created by jan on 12-11-2015.
 */
// todo jan rename class til mere generelt navn.
public class PointInfoDbContract {


    public PointInfoDbContract()
    {}


    public abstract class LoebsAktivitetDb implements BaseColumns
    {
        public static final String TABLE_NAME = "LoebsAktivitet";

        //public static final String COLUMN_NAME_NULLABLE = "XXX";  // ikke behov for null.

        public static final String COLUMN_NAME_LOEBSAKTIVITET_LOEBSAKTIVITETS_ID = "loebsaktivitetid";
        public static final String COLUMN_NAME_LOEBSAKTIVITET_NOTE = "note";
        public static final String COLUMN_NAME_LOEBSAKTIVITET_NAVN = "navn";
        public static final String COLUMN_NAME_LOEBSAKTIVITET_EMAIL = "email";
        public static final String COLUMN_NAME_LOEBSAKTIVITET_PERSONID = "personid";    // kan være studienummer.
        public static final String COLUMN_NAME_LOEBSAKTIVITET_STARTTIDSPUNKT = "starttidspunkt";
        public static final String COLUMN_NAME_LOEBSAKTIVITET_PROGRAMTYPE = "programtype";
    }


    public abstract class PointInfoDb implements BaseColumns
    {
        public static final String TABLE_NAME = "PointInfo";

        //public static final String COLUMN_NAME_NULLABLE = "loebsaktivitetid";

        public static final String COLUMN_NAME_LOEBS_ID = "loebsaktivitetid";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";            // kan fjernes.
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
    public void insertPointData(PointInfo pointInfo, UUID loebsAktivitetId, Context context)
    {
        Log.d("JJdatabase", "insertPointData");

        // Gets the data repository in write mode
        DatabaseContract sqliteRepo = new DatabaseContract(context);
        SQLiteDatabase db = sqliteRepo.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PointInfoDbContract.PointInfoDb.COLUMN_NAME_ENTRY_ID, "1");  // ????
        values.put(PointInfoDbContract.PointInfoDb.COLUMN_NAME_LOEBS_ID, loebsAktivitetId.toString());
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

    public UUID insertLoebsAktivitet(LoebsAktivitet loebsAktivitet, Context context)
    {
        Log.d("JJdatabase", "insertPointData");

        // Gets the data repository in write mode
        DatabaseContract sqliteRepo = new DatabaseContract(context);
        SQLiteDatabase db = sqliteRepo.getWritableDatabase();


        // UUID loebsAktivitetId = UUID.randomUUID();
        Time starttidspunkt = new Time();
        starttidspunkt.setToNow();

        ContentValues values = new ContentValues();
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_LOEBSAKTIVITETS_ID, loebsAktivitet.getLoebsAktivitetUuid().toString());
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_STARTTIDSPUNKT, loebsAktivitet.getStarttidspunkt());
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_NOTE, loebsAktivitet.getLoebsNote());
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_EMAIL, "TBD");
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_NAVN, "TBD");
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_PERSONID, "TBD");
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_PROGRAMTYPE, "Normal");  // todo jan 14/11-15.

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                LoebsAktivitetDb.TABLE_NAME,
                null,
                values);

        return loebsAktivitet.getLoebsAktivitetUuid();
    }


}
