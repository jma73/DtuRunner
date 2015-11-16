package dk.dtu.itdiplom.dturunner;


import android.content.Intent;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dk.dtu.itdiplom.dturunner.Database.DatabaseHelper;
import dk.dtu.itdiplom.dturunner.Model.LoebsAktivitet;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLoebsHistorik extends Fragment implements AdapterView.OnItemClickListener {

    List<LoebsAktivitet> loebsAktivitetList;

    public FragmentLoebsHistorik() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rod = inflater.inflate(R.layout.fragment_loebs_historik, container, false);

        DatabaseHelper databaseHelper = new DatabaseHelper();
        loebsAktivitetList = databaseHelper.hentLoebsAktivitetListe(getActivity());

        ListView listView = (ListView) rod.findViewById(R.id.listViewLoebsHistorik);
        listView.setOnItemClickListener(this);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, loebsAktivitetList);

        listView.setAdapter(adapter);
//        listView.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_2, loebsAktivitetList));

        return rod;
    }


//    @Override
//    public void onClick(View v) {
//
//        Toast.makeText(getContext(), "on cklick ...", Toast.LENGTH_SHORT);
//
////        if(v==)
//
//    }


    //  pt. ikke ok...
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getActivity(), "Klik på " + position + ", id=" + id, Toast.LENGTH_SHORT).show();

        LoebsAktivitet loebsAktivitet = loebsAktivitetList.get((int) id);

        Toast.makeText(getActivity(), "uuid: " + loebsAktivitet.getLoebsAktivitetUuid(), Toast.LENGTH_SHORT).show();

        Log.d("FragmentLoebsHistorik", "Løbsinfo:" + id + loebsAktivitet.getTextLog());

//        PopupWindow pw = new PopupWindow(getActivity());
////        PopupWindow pw = new PopupWindow(getActivity(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        TextView textView = new TextView(getActivity());
//        textView.setText("popopop up");
//        pw.showAsDropDown(textView);
//
//        // test detailsview:
//        TextView tv = new TextView(getActivity());


//        sendEmail();

//        visPersonInfo(); // denne har virket!




        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        // todo jan send id med over!
//
//
        Fragment fragment = new FragmentLoebsAktivitet();
        Fragment fragmentPersonInfo = new FragmentPersonInfo();
//
        Bundle bundle = new Bundle();
        bundle.putString("Uuid", String.valueOf(loebsAktivitet.getLoebsAktivitetUuid()));
        fragment.setArguments(bundle);
//
        fragmentTransaction.replace(R.id.frameLayoutContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }


    private void sendEmail() {
        String emailTo = "jma73android@gmail.com";
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        i.putExtra(Intent.EXTRA_SUBJECT, "EXTRA_SUBJECT");
        i.putExtra(Intent.EXTRA_TEXT, "EXTRA_TEXT");
        i.putExtra(Intent.EXTRA_CC, new String[]{"jma73android@gmail.com"});
        startActivity(Intent.createChooser(i, "Send e-post..."));
    }
}
