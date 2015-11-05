package dk.dtu.itdiplom.dturunner.Utils;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
}
