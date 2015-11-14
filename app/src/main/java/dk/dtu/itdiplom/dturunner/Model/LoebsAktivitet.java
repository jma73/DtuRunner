package dk.dtu.itdiplom.dturunner.Model;

//import java.sql.Time;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    String loebsNote;
    String loebsDato;   // kan vel udgå
    String loebsProgramType;
    public List<PointInfo> pointInfoList;
    long starttidspunkt;

    public UUID getLoebsAktivitetUuid() {
        return loebsAktivitetUuid;
    }

    UUID loebsAktivitetUuid;

    public LoebsAktivitet() {

        pointInfoList = new ArrayList<PointInfo>();
        Time time = new Time();
        time.setToNow();
        starttidspunkt = time.toMillis(false);
        loebsAktivitetUuid = UUID.randomUUID();

        loebsDato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

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
