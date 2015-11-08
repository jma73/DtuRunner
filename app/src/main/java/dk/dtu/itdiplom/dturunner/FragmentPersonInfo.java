package dk.dtu.itdiplom.dturunner;


import android.content.Context;
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

import java.io.FileOutputStream;

import dk.dtu.itdiplom.dturunner.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPersonInfo extends Fragment implements View.OnClickListener {

    Button buttonPersonInfoSet;
    private EditText editTextPersonNavn;
    private EditText editTextStudienummer;

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
        buttonPersonInfoSet.setOnClickListener(this);

        loadSharedPrefs();


        return rod;
    }

    private void loadSharedPrefs() {
        SharedPreferences pref = getActivity().getPreferences(0);
        String navn = pref.getString("personnavn", "");
        String studienummer = pref.getString("studienummer", "");
        if(navn != "")
        {
            editTextPersonNavn.setText(navn);
        }
        editTextStudienummer.setText(studienummer);
    }


    @Override
    public void onClick(View v) {
        if(v == buttonPersonInfoSet)
        {
            GemPersonInfo();
        }
    }

    private void GemPersonInfo() {

        Log.d("JJJ", "i GemPersonInfo");
        String navn = editTextPersonNavn.getText().toString();
        String studienummer = editTextStudienummer.getText().toString();

        SharedPreferences pref = getActivity().getPreferences(0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("personnavn", navn);
        edt.putString("studienummer", studienummer);
        edt.commit();

        Toast.makeText(getActivity(), "Oplysningerne er gemt!", Toast.LENGTH_LONG);

//
//        SharedPreferences pref = getS
//
//                getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//
//        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//        editor.putString("name", "Elena");
//        editor.putInt("idName", 12);
//        editor.commit();

//        SharedPreferences sharedPreferences = getSharedPreferences()

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
