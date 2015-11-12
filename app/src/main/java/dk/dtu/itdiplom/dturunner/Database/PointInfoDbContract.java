package dk.dtu.itdiplom.dturunner.Database;

import android.provider.BaseColumns;

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

}
