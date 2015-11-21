package dk.dtu.itdiplom.dturunner.Model.Entities;

import java.util.List;

import dk.dtu.itdiplom.dturunner.Model.PointInfo;

/**
 * Created by JanMøller on 08-11-2015.
 */
public class LoebsInfo {



    List<PointInfo> pointInfoList;



    // entitet til SqlLite Database og
    // Kolonne navne
    String loebsId = "loebsId";
    String loebsNavn = "loebsNavn";
    String loebsDatetime = "loebsDatetime";
}
