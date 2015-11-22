package dk.dtu.itdiplom.dturunner.Views;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dk.dtu.itdiplom.dturunner.Model.Entities.GlobaleKonstanter;
import dk.dtu.itdiplom.dturunner.R;

/**
 * Giver mulighed for at indtaste oplysninger om brugeren.
 */
public class FragmentPersonInfo extends Fragment implements View.OnClickListener {

    Button buttonPersonInfoSet;
    private EditText editTextPersonNavn;
    private EditText editTextStudienummer;
    private EditText editTextEmail;

    public FragmentPersonInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rod = inflater.inflate(R.layout.fragment_person_info, container, false);
        buttonPersonInfoSet = (Button) rod.findViewById(R.id.buttonPersonInfoSet);
        editTextPersonNavn = (EditText) rod.findViewById(R.id.editTextPersonNavn);
        editTextStudienummer = (EditText) rod.findViewById(R.id.editTextStudienummer);
        editTextEmail = (EditText) rod.findViewById(R.id.editTextEmail);
        buttonPersonInfoSet.setOnClickListener(this);

        loadSharedPrefs();


        return rod;
    }

    private void loadSharedPrefs() {
        SharedPreferences pref = getActivity().getPreferences(0);


        String navn = pref.getString(GlobaleKonstanter.PREF_PERSONNAVN, "");
        String email = pref.getString(GlobaleKonstanter.PREF_EMAIL, "");
        String studienummer = pref.getString(GlobaleKonstanter.PREF_STUDIENUMMER, "");
        if(navn != "")
        {
            editTextPersonNavn.setText(navn);
        }
        editTextStudienummer.setText(studienummer);
        editTextEmail.setText(email);
    }


    @Override
    public void onClick(View v) {
        if(v == buttonPersonInfoSet)
        {
            GemPersonInfo();
        }
    }

    private void GemPersonInfo() {
        if(getActivity() == null) return;   // hvis activity context er null er vi allerede ude af fragment.

        Log.d("JJJ", "i GemPersonInfo");

        String navn = editTextPersonNavn.getText().toString();
        String studienummer = editTextStudienummer.getText().toString();
        String email = editTextEmail.getText().toString();

        SharedPreferences pref = getActivity().getPreferences(0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString(GlobaleKonstanter.PREF_PERSONNAVN, navn);
        edt.putString(GlobaleKonstanter.PREF_EMAIL, email);
        edt.putString(GlobaleKonstanter.PREF_STUDIENUMMER, studienummer);
        edt.commit();

        Toast.makeText(getActivity(), "Dine oplysninger er gemt!", Toast.LENGTH_SHORT).show();

    }

    /*
        Send til email.
         - skal udvides... attatche en fil. indlæsning af email.
     */
    public void SendEmailIntent(String content)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "jma73android@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "DtuRunner");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body. Jan.\n\n\nGPS data\n\n\n" + content );
        //intent.putExtra(Intent.ACTION_ATTACH_DATA, "I'm email body. Jan." + content );

        startActivity(Intent.createChooser(intent, "Send Email"));

    }



}
