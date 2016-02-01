package dk.dtu.itdiplom.dturunner.Utils;

import android.location.Location;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dk.dtu.itdiplom.dturunner.Model.PointInfo;
import dk.dtu.itdiplom.dturunner.SingletonDtuRunner;

/**
 * Created by JanMøller on 05-11-2015.
 */
public class LocationUtils {

    /*
        Med float.
     */
//    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
//        double earthRadius = 6371000; //meters
//        double dLat = Math.toRadians(lat2-lat1);
//        double dLng = Math.toRadians(lng2-lng1);
//        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                        Math.sin(dLng/2) * Math.sin(dLng/2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//        float dist = (float) (earthRadius * c);
//
//        return dist;
//    }

//    public static double distFromDouble(double latitude1, double longitude1, double latitude2, double longitude2) {
//        float distance = distFrom((float)latitude1, (float)longitude1, (float)latitude2, (float)longitude2  );
//
//        Log.d("jj", "distance: "+distance);
//
//        return (double) distance;
//    }

    /*
        Med double.
     */
    public static double getDistanceBetweenPoints(double lat1, double lng1, double lat2, double lng2 ){
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        final double distanceInMeters = earthRadius * c;
        return distanceInMeters;
//        double distanceInMetersRoundedToInt = Math.round(distanceInMeters);
//        return distanceInMetersRoundedToInt;
    }


    /*
    min egen test. skal nok bare slettes...
      http://stackoverflow.com/questions/21410698/calculating-speed-for-a-navigation-app-without-getspeed-method
     */
    long getSpeedMetersPerSecond(Location location, long distanceInMeters)
    {
        long lastTimeStamp = location.getTime();
        long timeDelta = (location.getTime() - lastTimeStamp)/1000;

        long speed = 0;
        if(timeDelta > 0){
            speed = (distanceInMeters/timeDelta);
        }
        return speed;
    }

    /*
        Det er vist blot dette der skal til...
     */
    public static double getSpeedMetersPerSecond(double distanceInMeters, long timeDelta)
    {
        double speed = (distanceInMeters/timeDelta) * 1000;
        return speed;
    }

    /*
        Med PointInfo.
        Enhed: meter(?)
     */
    public static double getDistanceBetweenPoints(PointInfo location1, PointInfo location2){

        double lat1 = location1.getLatitude();
        double lng1 = location1.getLongitude();
        double lat2 = location2.getLatitude();
        double lng2 = location2.getLongitude();

        return getDistanceBetweenPoints(lat1, lng1, lat2, lng2);
    }

    /*
        Enhed: m/s (meter pr. sekund)
     */
    public static double getSpeedBetweenPoints(PointInfo location1, PointInfo location2){

        // todo jan - 8/11-15: Stadig under test!!!
        double distanceInMeters = getDistanceBetweenPoints(location1, location2);

        long time1 = location1.getTimestamp();
        long time2 = location2.getTimestamp();

        long secondsPassed = (location2.getTimestamp() - location1.getTimestamp()) / 1000;

        Log.d("jj", "distanceInMeters: " + distanceInMeters);
        Log.d("jj", "secondsPassed: " + secondsPassed);
        Log.d("jj", "time1: " + time1);
        Log.d("jj", "time2: " + time2);

        if(distanceInMeters == 0)
            return 0;

        return distanceInMeters / secondsPassed;
    }

    /*
        Enhed: m/s (meter pr. sekund)

        Denne metode udregner gennemsnitshastigheden for de seneste x punkter.
        Foreløbig er x = 4, men dette er valgt ret tilfældigt, så det kan overvejes om det skal ændres.
        Jo højere tal, jo længere tid går der inden der kan beregnes en hastighed. jo færre -> jo mere upræcist.
     */
    public static double getAverageSpeedLatestPoints(List<PointInfo> pointInfoListe)
    {
        int size = pointInfoListe.size();
        double avgSpeed = 0;
//        PointInfo lastPointInfo = pointInfoListe.get(size -1);
        Log.d("jjLocationUtils", "Tester avg speed: " + avgSpeed);

        int maxNumberOfPointToGoThrugh = 4;
        if(size < maxNumberOfPointToGoThrugh)
        {
            maxNumberOfPointToGoThrugh = size -1;
        }
        for (int i = size -1; i > size - maxNumberOfPointToGoThrugh; i--)
        {
            avgSpeed += getSpeedBetweenPoints(pointInfoListe.get(i - 1), pointInfoListe.get(i));
            Log.d("jjLocationUtils", String.format("Tester avg speed: %s , %s . i=%s.", avgSpeed, maxNumberOfPointToGoThrugh, i));
        }
        return avgSpeed / (maxNumberOfPointToGoThrugh - 1);
    }

    /*
    * udregner gennemsnitsfarten for alle punkter siden start.
    * */
    public static double getAverageSpeedFromStart(List<PointInfo> pointInfoList)
    {
        if(pointInfoList.size() == 0)
            return 0;

        double distance = getTotalDistance(pointInfoList);

        long milliseconds = getTimeMillisecondsSinceStart(pointInfoList.get(0), pointInfoList.get(pointInfoList.size() - 1));
        return getSpeedMetersPerSecond(distance, milliseconds);
    }

    /*
        Med Location objektet
     */
    public static double getDistanceBetweenPoints(Location location1, Location location2){

        double lat1 = location1.getLatitude();
        double lng1 = location1.getLongitude();
        double lat2 = location2.getLatitude();
        double lng2 = location2.getLongitude();


//        return (long) distFromDouble(lat1, lng1, lat2, lng2);
        return getDistanceBetweenPoints(lat1, lng1, lat2, lng2);

//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lng2 - lng1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//                + Math.cos(Math.toRadians(lat1))
//                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
//                * Math.sin(dLon / 2);
//        double c = 2 * Math.asin(Math.sqrt(a));
//        long distanceInMeters = Math.round(6371000 * c);
//        return distanceInMeters;
    }

    public static double getSpeedBetweenPoints(Location location1, Location location2){

        // todo jan - 8/11-15: Stadig under test!!!
        double distanceInMeters = getDistanceBetweenPoints(location1, location2);

        long time1 = location1.getTime();
        long time2 = location2.getTime();

        long secondsPassed = (location2.getTime() - location1.getTime()) / 1000;

        if(SingletonDtuRunner.erUnderUdviklingFlag) {
            Log.d("jj", "distanceInMeters: " + distanceInMeters);
            Log.d("jj", "secondsPassed: " + secondsPassed);
            Log.d("jj", "time1: " + time1);
            Log.d("jj", "time2: " + time2);
        }
        
        if(distanceInMeters == 0)
            return 0;

        return distanceInMeters / secondsPassed;
    }

    public static long getTimeMillisecondsSinceStart(Location location0, Location locationN) {
        return locationN.getTime() - location0.getTime();
    }

    public static String displayTimerFormat(long milliseconds)
    {
        // long milliseconds = getTimeMillisecondsSinceStart(location0, locationN);
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    public static long getTimeMillisecondsSinceStart(PointInfo location0, PointInfo locationN) {
        return locationN.getTimestamp() - location0.getTimestamp();
    }

    /*
    * Løber alle punkter igennem og beregner afstanden mellem hvert punkt, som akkumuleres.
    * Enhed: meter
    *
     */
    public static double getTotalDistance(List<PointInfo> pointInfoList) {

        PointInfo point1 = null, point2 = null;
        double totalDistance = 0;
        for (PointInfo pi :pointInfoList) {
            point1 = pi;
            if(point2 == null)
            {
                point2 = point1;
                continue;
            }
            else
            {
                totalDistance += getDistanceBetweenPoints(point1, point2);
            }

            point2 = point1;
        }

        return totalDistance;
    }

}
