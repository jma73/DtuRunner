package dk.dtu.itdiplom.dturunner.Views;


import android.os.Bundle;
//import android.app.Fragment;      // note jan: skiftet til support v4.
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.dtu.itdiplom.dturunner.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentForside extends Fragment {


    public FragmentForside() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forside, container, false);
    }


}
