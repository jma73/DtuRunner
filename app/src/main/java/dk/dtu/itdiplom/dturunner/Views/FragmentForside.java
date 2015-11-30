package dk.dtu.itdiplom.dturunner.Views;


import android.os.Bundle;
//import android.app.Fragment;      // note jan: skiftet til support v4.
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.dtu.itdiplom.dturunner.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentForside extends Fragment implements View.OnClickListener {

    Button buttonTestLocation;

    public FragmentForside() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_forside, container, false);

        buttonTestLocation = (Button) inflate.findViewById(R.id.buttonTestLocation);
        buttonTestLocation.setOnClickListener(this);

        return inflate;
    }

    public void buttonHandlerTestLocation(View view) {

        // todo jan 16/11-15: denne skal formentlig udgå igen!!!

//        FragmentShowOnMap fragmentShowOnMap = new FragmentShowOnMap();
//        FragmentShowOnMap2 fragmentShowOnMap2 = new FragmentShowOnMap2();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentShowOnMap2());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        if(v==buttonTestLocation)
        {
            buttonHandlerTestLocation(v);
        }
    }
}
