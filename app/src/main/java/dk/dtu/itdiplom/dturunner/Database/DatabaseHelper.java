package dk.dtu.itdiplom.dturunner.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.format.Time;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dk.dtu.itdiplom.dturunner.Model.Entities.LoebsAktivitet;
import dk.dtu.itdiplom.dturunner.Model.PointInfo;

/**
 * Created by jan on 12-11-2015.
 */
// todo jan rename class til mere generelt navn.
public class DatabaseHelper {

    final String LOGTAG = "jjDatabaseHelper";

    public DatabaseHelper()
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

    }

    // todo jan - tror nok at jeg skal flytte denne til sin egen klasse...
    public void insertPointData(PointInfo pointInfo, UUID loebsAktivitetId, Context context)
    {
        Log.d(LOGTAG, "insertPointData");

        // Gets the data repository in write mode
        DatabaseContract sqliteRepo = new DatabaseContract(context);
        SQLiteDatabase db = sqliteRepo.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.PointInfoDb.COLUMN_NAME_ENTRY_ID, "1");  // Anvendes ikke!
        values.put(DatabaseHelper.PointInfoDb.COLUMN_NAME_LOEBS_ID, loebsAktivitetId.toString());
        values.put(DatabaseHelper.PointInfoDb.COLUMN_NAME_LATITUDE, pointInfo.getLatitude());
        values.put(DatabaseHelper.PointInfoDb.COLUMN_NAME_LONGITUDE, pointInfo.getLongitude());
        values.put(DatabaseHelper.PointInfoDb.COLUMN_NAME_DISTANCE, pointInfo.getDistance());
        values.put(DatabaseHelper.PointInfoDb.COLUMN_NAME_SPEED, pointInfo.getSpeed());
        values.put(DatabaseHelper.PointInfoDb.COLUMN_NAME_HEARTRATE, pointInfo.getHeartRate());
        values.put(DatabaseHelper.PointInfoDb.COLUMN_NAME_TIMESTAMP, pointInfo.getTimestamp());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DatabaseHelper.PointInfoDb.TABLE_NAME,
                null,
                values);
        db.close();
    }

    public UUID insertLoebsAktivitet(LoebsAktivitet loebsAktivitet, Context context)
    {
        Log.d(LOGTAG, "insertLoebsAktivitet");

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
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_EMAIL, loebsAktivitet.getEmail());
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_NAVN, loebsAktivitet.getNavnAlias());
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_PERSONID, loebsAktivitet.getPersonId());
        values.put(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_PROGRAMTYPE, "Normal");  // todo jan 14/11-15.

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                LoebsAktivitetDb.TABLE_NAME,
                null,
                values);

        db.close();

        return loebsAktivitet.getLoebsAktivitetUuid();
    }

    public boolean sletLoebsAktivitet(Context context, UUID uuid)
    {
        Log.d(LOGTAG, "SletLoebsAktivitet");

        // Gets the data repository in write mode
        DatabaseContract sqliteRepo = new DatabaseContract(context);
        SQLiteDatabase db = sqliteRepo.getWritableDatabase();

        int rows = db.delete(LoebsAktivitetDb.TABLE_NAME, LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_LOEBSAKTIVITETS_ID + " = '" + uuid + "'", null);
        db.close();

        return rows > 0;
    }


    public boolean sletPointInfos(Context context, UUID uuid)
    {
        Log.d(LOGTAG, "sletPointInfos");

        DatabaseContract sqliteRepo = new DatabaseContract(context);
        SQLiteDatabase db = sqliteRepo.getWritableDatabase();

        int rows = db.delete(PointInfoDb.TABLE_NAME, PointInfoDb.COLUMN_NAME_LOEBS_ID + " = '" + uuid + "'", null);
        db.close();

        Log.d(LOGTAG, "sletPointInfos: Der blev slettet " + rows +" punkter hørende til aktiviteten.");

        return rows >= 0;  // der behøver ikke være nogle punkter pt. Skal nok sørge for at en løbsaktivitet uden punkter ikke bliver gemt.
    }

    // todo sql til hent af løbsaktivitet pr. uuid
    public LoebsAktivitet hentLoebsAktivitet(Context context, UUID uuid)
    {
        DatabaseContract sqliteRepo = new DatabaseContract(context);
        SQLiteDatabase db = sqliteRepo.getReadableDatabase();

        List<LoebsAktivitet> liste = new ArrayList<>();

        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", LoebsAktivitetDb.TABLE_NAME, LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_LOEBSAKTIVITETS_ID, uuid.toString());
        //db.rawQuery(query, null);

        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    String uuidAsString = cursor.getString(cursor.getColumnIndex(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_LOEBSAKTIVITETS_ID));
                    UUID loebsAktivitetsUUID = UUID.fromString(uuidAsString);
                    LoebsAktivitet loebsAktivitet = new LoebsAktivitet(loebsAktivitetsUUID);

                    String starttid = cursor.getString(cursor.getColumnIndex(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_STARTTIDSPUNKT));
                    loebsAktivitet.setLoebsDato(starttid);
                    loebsAktivitet.setStarttidspunkt(cursor.getLong(cursor.getColumnIndex(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_STARTTIDSPUNKT)));

                    List<PointInfo> pointInfoList = hentPointInfoList(context, loebsAktivitetsUUID);
                    loebsAktivitet.pointInfoList = pointInfoList;

                    // todo jan: skal have lavet beregnere på LoebsAktivitet til at beregne distance, gennemsnits fart mm.

//                    loebsAktivitet.setLoebsNote(String.format(" Dette er en test: Antal løbspunkter: %s , Distance %s meter.",
//                            pointInfoList.size(), loebsAktivitet.getTotalDistanceMeters()));

                    liste.add(loebsAktivitet);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DatabaseFejl.DTURunner", "Error while trying to get posts from database... " + query);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }

        if(liste.size() > 1)
        {
            Log.e(LOGTAG, String.format("Der blev fundet flere LoebsAktivitet for uuid %s. Det burde ikke kunne ske...", uuid));
        }

        return liste.get(0);

    }

    private List<PointInfo> hentPointInfoList(Context context, UUID loebsAktivitetsUUID) {

        DatabaseContract sqliteRepo = new DatabaseContract(context);
        SQLiteDatabase db = sqliteRepo.getReadableDatabase();

        List<PointInfo> pointInfoList = new ArrayList<>();

        // todo jan working here

        String query = String.format("SELECT * FROM %s WHERE %s = '%s'", PointInfoDb.TABLE_NAME, PointInfoDb.COLUMN_NAME_LOEBS_ID, loebsAktivitetsUUID.toString());

        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    double latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(PointInfoDb.COLUMN_NAME_LATITUDE)));
                    double longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(PointInfoDb.COLUMN_NAME_LONGITUDE)));

                    long timestamp  = Long.parseLong(cursor.getString(cursor.getColumnIndex(PointInfoDb.COLUMN_NAME_TIMESTAMP)));
                    int heartRate = -1;//   cursor.getString(cursor.getColumnIndex(PointInfoDb.COLUMN_NAME_HEARTRATE)); // todo jan - må komme i en senere version
                    double speed = Double.parseDouble(cursor.getString(cursor.getColumnIndex(PointInfoDb.COLUMN_NAME_DISTANCE)));
                    double distance = Double.parseDouble(cursor.getString(cursor.getColumnIndex(PointInfoDb.COLUMN_NAME_DISTANCE)));
                    PointInfo pointInfo = new PointInfo(timestamp, latitude, longitude, speed, distance, heartRate) ;

                    pointInfoList.add(pointInfo);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DatabaseFejl.DTURunner", "Error while trying to get posts from database... " + query);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }



        return pointInfoList;
    }

    public List<LoebsAktivitet> hentLoebsAktivitetListe(Context context)
    {
        DatabaseContract sqliteRepo = new DatabaseContract(context);
        SQLiteDatabase db = sqliteRepo.getReadableDatabase();

        List<LoebsAktivitet> liste = new ArrayList<>();

        String query = String.format("SELECT * FROM %s ", LoebsAktivitetDb.TABLE_NAME);

        //db.execSQL(query);
        //db.query(query);
        db.rawQuery(query, null);

        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    // todo jan - skal udvides
                    String uuidAsString = cursor.getString(cursor.getColumnIndex(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_LOEBSAKTIVITETS_ID));
                    UUID loebsAktivitetsUUID = UUID.fromString(uuidAsString);
                    LoebsAktivitet loebsAktivitet = new LoebsAktivitet(loebsAktivitetsUUID);

                    String starttid = cursor.getString(cursor.getColumnIndex(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_STARTTIDSPUNKT));
                    loebsAktivitet.setLoebsDato(starttid);
                    loebsAktivitet.setStarttidspunkt(cursor.getLong(cursor.getColumnIndex(LoebsAktivitetDb.COLUMN_NAME_LOEBSAKTIVITET_STARTTIDSPUNKT)));

                    List<PointInfo> pointInfoList = hentPointInfoList(context, loebsAktivitetsUUID);
                    loebsAktivitet.pointInfoList = pointInfoList;

                    liste.add(loebsAktivitet);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DatabaseFejl.DTURunner", "Error while trying to get posts from database... " + query);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();     // hvornår skal jeg lukke database forbindelsen?
        }
        return liste;
    }

}
