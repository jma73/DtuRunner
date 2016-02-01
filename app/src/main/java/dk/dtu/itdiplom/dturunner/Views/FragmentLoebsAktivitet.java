package dk.dtu.itdiplom.dturunner.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

import dk.dtu.itdiplom.dturunner.Database.DatabaseHelper;
import dk.dtu.itdiplom.dturunner.Model.Entities.GlobaleKonstanter;
import dk.dtu.itdiplom.dturunner.Utils.FileHelper;
import dk.dtu.itdiplom.dturunner.Model.Entities.LoebsAktivitet;
import dk.dtu.itdiplom.dturunner.R;


/**
 * Visning af løb, med mulighed for slet, email løb.
 */
public class FragmentLoebsAktivitet extends Fragment implements View.OnClickListener {

    LoebsAktivitet loebsAktivitetSelected;
    Button buttonSendLoebsdataEmail, buttonSletLoebsAkt;
    private String uuid;
    private Button buttonVisLoebsAkt;

    public FragmentLoebsAktivitet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("jjj", "in FragmentLoebsAktivitet");

        // Inflate the layout for this fragment
        View rod = inflater.inflate(R.layout.fragment_loebs_aktivitet, container, false);

        uuid = "N/A";

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            uuid = bundle.getString("Uuid");
        }

        TextView tv = (TextView) rod.findViewById(R.id.textViewLoebsAktId);
        tv.setText(uuid);

        loebsAktivitetSelected = new DatabaseHelper().hentLoebsAktivitet(getActivity(), UUID.fromString(uuid));
        //loebsAktivitetSelected.pointInfoList    // todo jan - mangler at indlæse punkterne.

        String loebsAktivitetInfoString = String.format("Dato: %s \n" +
                "\tStarttidspunkt: %s \n\tDistance: %s \n\tTid: %s \n " +
                "\tGennemsnitshastighed: %s " +
                        "\n\tTid pr. km: %s " +
                        "\n\n Uuid %s ",
                loebsAktivitetSelected.getStarttimeFormatted(),
                //loebsAktivitetSelected.getStarttidspunkt(),
                loebsAktivitetSelected.displayFormattedTime(loebsAktivitetSelected.getStarttidspunkt()),
                // loebsAktivitetSelected.getTotalDistanceMeters(),
                loebsAktivitetSelected.getTotalDistanceMetersFormatted(true),

                loebsAktivitetSelected.getTimeSinceStartFormatted(),
                // loebsAktivitetSelected.getTimeMillisecondsSinceStart() / (60 / 60 * 1000),  // todo jan skal vises pænt. pt. i sekunder.
                loebsAktivitetSelected.getAverageSpeedFromStartFormatted(true),
                loebsAktivitetSelected.getAverageMinutesPerKilometer(true),
                uuid);

        tv.setText(loebsAktivitetInfoString);

        buttonSletLoebsAkt = (Button) rod.findViewById(R.id.buttonSletLoebsAkt);
        buttonSletLoebsAkt.setOnClickListener(this);
        buttonSendLoebsdataEmail = (Button) rod.findViewById(R.id.buttonSendLoebsdataEmail);
        buttonSendLoebsdataEmail.setOnClickListener(this);
        buttonVisLoebsAkt = (Button) rod.findViewById(R.id.buttonVisLoebsAkt);
        buttonVisLoebsAkt.setOnClickListener(this);

        return rod;
    }

    // todo jan - metoden skal flyttes ud.
    private void sendEmail() {
        SharedPreferences pref = getActivity().getPreferences(0);
        String email = pref.getString(GlobaleKonstanter.PREF_EMAIL, "");

        String emailTo = email;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        intent.putExtra(Intent.EXTRA_SUBJECT, "DtuRunner - løbsdata");
        intent.putExtra(Intent.EXTRA_TEXT, "Løbsdata fra ... dags dato.");
        //  intent.putExtra(Intent.EXTRA_CC, new String[]{"jma73android@gmail.com"});   // mulighed for cc adresse.

        boolean copyToFileWithDate = true;      // todo jan 1/2-2016: bruger bør kunne slå dette til/fra!
        File file = FileHelper.saveFileExternalStorage2(getActivity(), this.loebsAktivitetSelected, copyToFileWithDate);
        Uri uri = Uri.fromFile(file);

        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("text/plain");

        startActivity(Intent.createChooser(intent, "Send løbsdata med e-post..."));
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v == buttonSendLoebsdataEmail)
        {
            // todo jan - hent, gem til fil og send løbsdata...
            sendEmail();
        }
        if(v == buttonSletLoebsAkt)
        {
            sletLoebsAktivitet();
        }
        if(v == buttonVisLoebsAkt)
        {
            visRutePaaKort();
        }
    }

    private void sletLoebsAktivitet() {

        boolean sqlResult = new DatabaseHelper().sletLoebsAktivitet(getActivity(), UUID.fromString(uuid));
        if(sqlResult)
        {
            sqlResult = new DatabaseHelper().sletPointInfos(getActivity(), UUID.fromString(uuid));

            Toast.makeText(getActivity(), "Aktiviteten blev slettet", Toast.LENGTH_SHORT).show();

            // todo jan 16/12-15: slet også alle PointInfoDb where

            // tving Fragmentet til at "lukke":
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStackImmediate();
        } else
        {
            Toast.makeText(getActivity(), "Aktiviteten blev IKKE slettet", Toast.LENGTH_LONG).show();
        }
    }

    private void visRutePaaKort(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = new FragmentShowOnMap2();

        Bundle bundle = new Bundle();
        bundle.putString("Uuid", String.valueOf(uuid));
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frameLayoutContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}