package dk.dtu.itdiplom.dturunner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Visning af løb, med mulighed for slet, email løb.
 */
public class FragmentLoebsAktivitet extends Fragment implements View.OnClickListener {


    Button buttonSendLoebsdataEmail;

    public FragmentLoebsAktivitet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("jjj", "in FragmentLoebsAktivitet");

        // Inflate the layout for this fragment
        View rod = inflater.inflate(R.layout.fragment_loebs_aktivitet, container, false);

        String uuid = "N/A";

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            uuid = bundle.getString("Uuid");
        }

        TextView tv = (TextView) rod.findViewById(R.id.textViewLoebsAktId);
        tv.setText(uuid);

        // todo jan: hent og vis løbsAktivitet


        Button buttonSletLoebsAkt = (Button) rod.findViewById(R.id.buttonSletLoebsAkt);
        buttonSendLoebsdataEmail = (Button) rod.findViewById(R.id.buttonSendLoebsdataEmail);
        buttonSendLoebsdataEmail.setOnClickListener(this);
        return rod;
    }

    private void sendEmail() {
        // todo hent email fra SharedPrefs
        String emailTo = "jma73android@gmail.com";
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        i.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
        i.putExtra(Intent.EXTRA_TEXT, "EXTRA_TEXT");
        i.putExtra(Intent.EXTRA_CC, new String[]{"jma73android@gmail.com"});
        startActivity(Intent.createChooser(i, "Send løbsdata med e-post..."));
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
    }
}