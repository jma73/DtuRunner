package dk.dtu.itdiplom.dturunner.Views;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.dtu.itdiplom.dturunner.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentShowOnMap extends Fragment {


    public FragmentShowOnMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("jjTest", "onCreateView in FragmentShowOnMap");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_on_map, container, false);
    }


}
