package dk.dtu.itdiplom.dturunner.Model.Entities;

import android.support.annotation.NonNull;
import android.text.format.Time;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import dk.dtu.itdiplom.dturunner.Model.PointInfo;
import dk.dtu.itdiplom.dturunner.Utils.LocationUtils;

/**
 * Created by JanMøller on 09-11-2015.
 * Denne klasse indeholder de værdier som der er brug for ifb. med en løbsaktivitet.
 */
public class LoebsAktivitet {

    String email;
    String navnAlias;
    String personId;    // fx. studienummer
    String loebsNote;
    String loebsDato;   // kan vel udgå
    String loebsProgramType;        // kommer i en senere version
    public List<PointInfo> pointInfoList;
    long starttidspunkt;
    UUID loebsAktivitetUuid;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }


    public UUID getLoebsAktivitetUuid() {
        return loebsAktivitetUuid;
    }


    /*
        Denne konstructør skal anvendes når der startes et nyt løb.
        her new'es nyt uuid op, og tiden sættes.
     */
    public LoebsAktivitet() {

        pointInfoList = new ArrayList<PointInfo>();
        Time time = new Time();
        time.setToNow();
        starttidspunkt = time.toMillis(false);
        loebsAktivitetUuid = UUID.randomUUID();
        loebsDato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /*
       Denne konstructør skal anvendes når der læses op fra databasen
    */
    public LoebsAktivitet(UUID p_loebsAktivitetUuid)
    {
        loebsAktivitetUuid = p_loebsAktivitetUuid;
    }


    public String getTextHeader()
    {
        String date = getStarttimeFormatted();
        String distance = getTotalDistanceMetersFormatted(true);
        return String.format(" %s - Distance: %s ", date, distance);
    }

    @NonNull
    public String getStarttimeFormatted() {
        long milliseconds = Long.parseLong(loebsDato);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(milliseconds));
    }

    public String getTextLog()
    {
        String date = getStarttimeFormatted();
        return String.format(" :: %s %s %s %s ", date, loebsDato, loebsAktivitetUuid, starttidspunkt);
    }

    // note jan: toString giver teksten i ListView'et / adapteren.
    public String toString()
    {
        return getTextHeader();
    }

    public String getNavnAlias() {
        return navnAlias;
    }

    public void setNavnAlias(String navnAlias) {
        this.navnAlias = navnAlias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoebsNote()
    {
        return loebsNote;
    }

    public void setLoebsNote(String value)
    {
        loebsNote = value;
    }

    public String getLoebsDato() {
        return loebsDato;
    }

    public void setLoebsDato(String loebsDato) {
        this.loebsDato = loebsDato;
    }

    public long getStarttidspunkt() {
        return starttidspunkt;
    }

    public void setStarttidspunkt(long starttidspunkt) {
        this.starttidspunkt = starttidspunkt;
    }

    public String getLoebsProgramType() {
        return loebsProgramType;
    }

    public void setLoebsProgramType(String loebsProgramType) {
        this.loebsProgramType = loebsProgramType;
    }

    public double getCurrentSpeedSinceLastPoint()
    {
        int numberOfPoints = this.pointInfoList.size();
        if(numberOfPoints < 2)
            return 0;

        final PointInfo lastLocation = this.pointInfoList.get(numberOfPoints - 1);

        double distance = LocationUtils.getDistanceBetweenPoints(this.pointInfoList.get(numberOfPoints - 2), lastLocation);


        double speedSinceLast = LocationUtils.getSpeedBetweenPoints(this.pointInfoList.get(numberOfPoints - 2), lastLocation);
        //double speedSinceStartAverage = LocationUtils.getSpeedBetweenPoints(SingletonDtuRunner.loebsStatus.locationList.get(0), SingletonDtuRunner.loebsStatus.mCurrentLocation);

        return speedSinceLast;
    }

    public double getAverageSpeedLatestPoints()
    {
        int numberOfPoints = this.pointInfoList.size();
        if(numberOfPoints < 2)
            return 0;

        double averageSpeed = LocationUtils.getAverageSpeedLatestPoints(pointInfoList);
        return averageSpeed;
    }

    public double getTotalDistanceMeters()
    {
        double distance = 0;
        if(pointInfoList == null)
        {
            Log.d("LoebsAktivitet", "pointInfoList er null. Dette er en fejl! todo jan...");
            return distance;
        }

        if(pointInfoList.size() > 0)
            distance = LocationUtils.getTotalDistance(pointInfoList);

        return distance;
    }

    public String getTotalDistanceMetersFormatted(boolean medEnhedsAngivelse)
    {
        double distance = getTotalDistanceMeters();
        if(medEnhedsAngivelse){
            if(distance > 1200)
                return String.format("%.1f km", distance / 1000);
            return String.format("%.1f m", distance);
        }
        return String.format("%.1f", distance);
    }

    public double getAverageSpeedFromStart()
    {
        return LocationUtils.getAverageSpeedFromStart(pointInfoList);
    }

    public long getTimeMillisecondsSinceStart()
    {
        if(pointInfoList.size() == 0)
            return 0;

        return LocationUtils.getTimeMillisecondsSinceStart(pointInfoList.get(0), pointInfoList.get(pointInfoList.size() - 1));
    }

    public void nulstil() {
//        email;
//        navnAlias;
//        personId;    // fx. studienummer
//        loebsNote;

//        loebsDato;
//        loebsProgramType;
        pointInfoList.clear();
//        starttidspunkt;
        //UUID loebsAktivitetUuid;
    }
}
