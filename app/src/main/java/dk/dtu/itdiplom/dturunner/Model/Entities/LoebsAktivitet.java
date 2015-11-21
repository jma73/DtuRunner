package dk.dtu.itdiplom.dturunner.Model.Entities;

//import java.sql.Time;
import android.support.annotation.NonNull;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import dk.dtu.itdiplom.dturunner.Model.PointInfo;

/**
 * Created by JanMøller on 09-11-2015.
 * Denne klasse skal indeholde de værdier som der er brug for ifb. med en løbsaktivitet.
 */
public class LoebsAktivitet {

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

    String email;
    String navnAlias;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    String personId;    // fx. studienummer
    String loebsNote;
    String loebsDato;   // kan vel udgå
    String loebsProgramType;
    public List<PointInfo> pointInfoList;
    long starttidspunkt;

    public UUID getLoebsAktivitetUuid() {
        return loebsAktivitetUuid;
    }

    UUID loebsAktivitetUuid;

    /*
        Denne konstructør skal anvendes når der startes et nyt løb.
        her new nyt uuid og tiden op.
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
        return String.format(" %s ", date);
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

}
