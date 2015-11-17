package dk.dtu.itdiplom.dturunner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import dk.dtu.itdiplom.dturunner.Database.DatabaseHelper;
import dk.dtu.itdiplom.dturunner.Model.FileHelper;
import dk.dtu.itdiplom.dturunner.Model.LoebsAktivitet;


/**
 * Visning af løb, med mulighed for slet, email løb.
 */
public class FragmentLoebsAktivitet extends Fragment implements View.OnClickListener {


    Button buttonSendLoebsdataEmail, buttonSletLoebsAkt;
    private String uuid;

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

        // todo jan: hent og vis løbsAktivitet


        LoebsAktivitet loebsAktivitetSelected = new DatabaseHelper().hentLoebsAktivitet(getActivity(), UUID.fromString(uuid));
        //loebsAktivitetSelected.pointInfoList    // todo jan - mangler at indlæse punkterne.

        String loebsAktivitetInfoString = String.format("Dato: %s \n" +
                " Starttidspunkt %s \n\tDistance: XX meter \n\tTid: yy minutter \n " +
                        " Uuid %s ",
                loebsAktivitetSelected.getStarttimeFormatted(), loebsAktivitetSelected.getStarttidspunkt(), uuid);

        tv.setText(loebsAktivitetInfoString);

        buttonSletLoebsAkt = (Button) rod.findViewById(R.id.buttonSletLoebsAkt);
        buttonSletLoebsAkt.setOnClickListener(this);
        buttonSendLoebsdataEmail = (Button) rod.findViewById(R.id.buttonSendLoebsdataEmail);
        buttonSendLoebsdataEmail.setOnClickListener(this);
        return rod;
    }

    private void sendEmail() {
        // todo hent email fra SharedPrefs
        String emailTo = "jma73android@gmail.com";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        intent.putExtra(Intent.EXTRA_SUBJECT, "DtuRunner - løbsdata");
        intent.putExtra(Intent.EXTRA_TEXT, "Løbsdata fra ... dags dato.");
        //  intent.putExtra(Intent.EXTRA_CC, new String[]{"jma73android@gmail.com"});   // mulighed for cc adresse.


        // todo jan 16/11-15: test af attach fil. Ser ud til at virke. dog kommer filen ikke videre, end at det ligner at den er vedhæftet i mail programmet.
        String filename = "dtuRunner.txt";

//        FileHelper.testSaveFile(getActivity(), " points points.....");
//        File file = new File(getActivity().getFilesDir(), filename);
//        Uri uri = Uri.fromFile(file);


        // ny til external
        File fff = FileHelper.testSaveFileExternalStorage(getActivity(), "some content...");
        //File externalFilesDir = getActivity().getExternalFilesDir(null);
        Uri uri2 = Uri.fromFile(fff);

//        try {
//            inputStream = getActivity().openFileInput(filename);
//            intent.putExtra(Intent.EXTRA_STREAM, new FileHelper().testReadFile(getActivity()));
//
//        } catch (FileNotFoundException e) {
//                e.printStackTrace();
//        }


        intent.putExtra(Intent.EXTRA_STREAM, uri2);
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
            // todo jan - hent, gem til fil og send løbsdata...
            sletLoebsAktivitet();
        }
    }

    private void sletLoebsAktivitet() {

        boolean sqlResult = new DatabaseHelper().sletLoebsAktivitet(getActivity(), UUID.fromString(uuid));
        if(sqlResult)
        {
            Toast.makeText(getActivity(), "Aktiviteten blev slettet", Toast.LENGTH_SHORT).show();

            // tving Fragmentet til at "lukke":
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStackImmediate();
        } else
        {
            Toast.makeText(getActivity(), "Aktiviteten blev IKKE slettet", Toast.LENGTH_LONG).show();
        }
    }
}