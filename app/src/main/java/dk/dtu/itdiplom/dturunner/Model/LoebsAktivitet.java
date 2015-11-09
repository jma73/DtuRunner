package dk.dtu.itdiplom.dturunner.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JanMøller on 09-11-2015.
 * Denne klasse skal indeholde de værdier som der er brug for ifb. med en løbsaktivitet.
 */
public class LoebsAktivitet {

    public LoebsAktivitet() {
        pointInfoList = new ArrayList<PointInfo>();
    }

    String loebsNote;
    String loebsDato;
    String loebsProgramType;
    public List<PointInfo> pointInfoList;
    Date starttidspunkt;

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

    public Date getStarttidspunkt() {
        return starttidspunkt;
    }

    public void setStarttidspunkt(Date starttidspunkt) {
        this.starttidspunkt = starttidspunkt;
    }

    public String getLoebsProgramType() {
        return loebsProgramType;
    }

    public void setLoebsProgramType(String loebsProgramType) {
        this.loebsProgramType = loebsProgramType;
    }
}
