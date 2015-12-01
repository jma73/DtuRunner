package dk.dtu.itdiplom.dturunner.Views;

import android.os.Bundle;
//import android.app.Fragment;      // note jan: skiftet til support v4.
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.dtu.itdiplom.dturunner.R;
import dk.dtu.itdiplom.dturunner.SingletonDtuRunner;
import dk.dtu.itdiplom.dturunner.Utils.BuildInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentForside extends Fragment implements View.OnClickListener {

    Button buttonHistorik, buttonTestLocation, buttonOm, buttonLoeb;
    private String LOGTAG = "jjFragmentForside";

    public FragmentForside() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_forside, container, false);

        buttonHistorik = (Button) inflate.findViewById(R.id.buttonHistory);
        buttonOm = (Button) inflate.findViewById(R.id.buttonOm);
        buttonLoeb = (Button) inflate.findViewById(R.id.buttonLoeb);
        buttonTestLocation = (Button) inflate.findViewById(R.id.buttonTestLocation);
        buttonOm.setOnClickListener(this);
        buttonLoeb.setOnClickListener(this);
        buttonHistorik.setOnClickListener(this);
        buttonTestLocation.setOnClickListener(this);

        return inflate;
    }

    public void buttonHandlerTestLocation() {

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

    public void buttonHandlerLoeb() {

        Log.d(LOGTAG, ":: i buttonHandlerLoeb.");

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(SingletonDtuRunner.fragmentLoebTag);
        if(fragment != null)
        {
            // todo jan - slet?
            Log.d(LOGTAG, ":: i buttonHandlerLoeb: Fragment with tag loeb er fundet!");
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentLoeb());
        fragmentTransaction.addToBackStack(SingletonDtuRunner.fragmentLoebTag);
        fragmentTransaction.commit();
    }

    public void buttonHandlerOm() {

        Log.d(LOGTAG, ":: i buttonHandlerOm.");
        SingletonDtuRunner.buildDate = BuildInfo.GetBuildDate(getActivity());

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentAbout());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void buttonHandlerLoebsHistorik() {
        Log.d(LOGTAG, ":: i buttonHandlerLoebsHistorik.");

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentLoebsHistorik());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        if(v==buttonLoeb)
        {
            buttonHandlerLoeb();
        }
        if(v==buttonHistorik)
        {
            buttonHandlerLoebsHistorik();
        }
        if(v==buttonOm)
        {
            buttonHandlerOm();
        }
        if(v ==buttonTestLocation)
        {
            buttonHandlerTestLocation();
        }

    }
}
