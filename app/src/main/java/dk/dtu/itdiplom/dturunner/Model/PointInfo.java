package dk.dtu.itdiplom.dturunner.Model;

import java.sql.Timestamp;

/**
 * Created by JanMøller on 08-11-2015.
 *
 * Denne klasse indeholder de data som er nødvendige at opsamle.
 */
public class PointInfo {

    Timestamp timestamp;
    double latitude;
    double longitude;
    double speed;
    double distance;
    int heartRate;

    // separator tegn. Kunne laves til at blive skiftet ud.
    String separator = "\t";

    public void PointInfo(Timestamp timestamp, double latitude, double longitude, double speed, double distance, int heartRate)
    {
        this.distance = distance;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.heartRate = heartRate;
    }

    public String ToString()
    {
        return timestamp + separator + latitude + separator + longitude + separator + speed + separator + distance + separator + heartRate;
    }

}
