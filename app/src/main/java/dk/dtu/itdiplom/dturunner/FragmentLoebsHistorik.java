package dk.dtu.itdiplom.dturunner;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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

        //ArrayList<String> ordliste = ForsideActivity.galgelogik.muligeOrd();

        Toast.makeText(getContext(), "on cklick ...", Toast.LENGTH_SHORT).show();



        LoebsAktivitet loebsAktivitet = loebsAktivitetList.get((int) id);
        Log.d("FragmentOrdliste", "Løbsinfo:" + id + loebsAktivitet.getTextHeader());

    }
}
