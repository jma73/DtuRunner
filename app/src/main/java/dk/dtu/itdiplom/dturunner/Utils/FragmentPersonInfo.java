package dk.dtu.itdiplom.dturunner.Utils;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.dtu.itdiplom.dturunner.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPersonInfo extends Fragment {


    public FragmentPersonInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person_info, container, false);
    }


}
