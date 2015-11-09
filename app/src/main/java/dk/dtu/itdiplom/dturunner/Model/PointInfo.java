package dk.dtu.itdiplom.dturunner.Model;

import java.sql.Timestamp;

/**
 * Created by JanMøller on 08-11-2015.
 *
 * Denne klasse indeholder de data som er nødvendige at opsamle.
 */
public class PointInfo {

    public PointInfo(long timestamp, double latitude, double longitude, double speed, double distance, int heartRate) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.distance = distance;
        this.heartRate = heartRate;
    }

    //Timestamp timestamp2;
    long timestamp;
    double latitude;
    double longitude;
    double speed;
    double distance;
    int heartRate;

    // separator tegn. Kunne laves til at blive skiftet ud.
    String separator = "\t";

//    public PointInfo(long timestamp, double latitude, double longitude, double speed, double distance, int heartRate)
//    {
//        this.distance = distance;
//        this.timestamp = timestamp;
//        this.latitude = latitude;
//        this.longitude = longitude;
//        this.speed = speed;
//        this.heartRate = heartRate;
//    }

    public String ToString()
    {
        return timestamp + separator + latitude + separator + longitude + separator + speed + separator + distance + separator + heartRate;
    }

}
