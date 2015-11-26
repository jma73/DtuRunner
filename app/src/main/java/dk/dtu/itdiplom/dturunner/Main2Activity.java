package dk.dtu.itdiplom.dturunner;

//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import dk.dtu.itdiplom.dturunner.Utils.BuildInfo;
import dk.dtu.itdiplom.dturunner.Views.FragmentAbout;
import dk.dtu.itdiplom.dturunner.Views.FragmentForside;
import dk.dtu.itdiplom.dturunner.Views.FragmentLoeb;
import dk.dtu.itdiplom.dturunner.Views.FragmentLoebsHistorik;
import dk.dtu.itdiplom.dturunner.Views.FragmentPersonInfo;
import dk.dtu.itdiplom.dturunner.Views.FragmentShowOnMap;
import dk.dtu.itdiplom.dturunner.Views.FragmentShowOnMap2;

public class Main2Activity extends AppCompatActivity implements FragmentAbout.OnFragmentInteractionListener
{
    SingletonDtuRunner singletonDtuRunner;

    // Global variables. skal placeres i singleton klasse???
    final String fragmentLoebTag = "FragmentLoeb";

    private static final String LOGTAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if(SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet)
        {
            Log.d(LOGTAG, "Der er allerede en løbs aktivitet startet...");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        // todo jan: skal tjekke på savedInstanceState
        //  if (savedInstanceState == null) {

        visMainMenuFragment(savedInstanceState == null);

        // dette er den floating email... bør bare fjernes.... todo jan. 1/11-2015...
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action (jan)", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //if (Build.VERSION.SDK_INT>=19) menu.add(0, 115, 0, "*Fuldskærm");

        menu.add(0, 102, 0, "*Vælg fil");
        menu.add(0, 110, 0, "Indtast personoplysninger");
        menu.add(0, 200, 0, "Til hovedskærm...");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOGTAG, " in onOptionsItemSelected " + item.getItemId());
        if (item.getItemId() == 200) {
            visMainMenuFragment(false);
        }
        else if (item.getItemId() == 110) {
//            visMainMenuFragment(false);
            visPersonInfo();        // todo jan NB der bliver flere hele tiden.
        }

//    else if (item.getItemId() == 115) {
//    if (Build.VERSION.SDK_INT>=19)
//    {
//        Log.d(LOGTAG, " in onOptionsItemSelected Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
//
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
//    }
//        }
        else if (item.getItemId() == 102) {
            Toast.makeText(this, "Kommer senere!!!", Toast.LENGTH_LONG);
        }

    return true;    // todo jan: skal der returnes andet?
    }


    /*
        En foreløb override af onBackPressed. Dette for at stoppe bruge fra at gå ud af løbssiden ned tilbage knappen, uden først at stoppe løbet.
     */
    @Override
    public void onBackPressed() {

        Log.d(LOGTAG, "- onBackPressed.... " + getSupportFragmentManager().getBackStackEntryCount());
        if(SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet)
        {
            // hvis der er et løb igang ønsker vi ikke at gå tilbage...
            Log.d(LOGTAG, "BackStackEntry for Fragment har ikke noget navnxxxxxxxx...");
        }

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++     ) {
            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
            if(entry.getName() != null)
                Log.d(LOGTAG, entry.getName());
            else
                Log.d(LOGTAG, "BackStackEntry for Fragment har ikke noget navn...");
        }

        if(getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            final String fragmentBackStackName = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();

            if(fragmentBackStackName != null &&  fragmentBackStackName == fragmentLoebTag)
            {
                Log.d(LOGTAG, "- onBackPressed - fragment found:::" + fragmentBackStackName);
                //this.finish();

                if(SingletonDtuRunner.loebsStatus.isLoebsAktivitetStartet)
                {
                    // hvis der er et løb igang ønsker vi ikke at gå tilbage...
                }
                else
                {

                    getSupportFragmentManager().popBackStack();
                }
                // todo jan 21/11-15: Kunne spørge om bruger virkelig vil forlade løbssiden...? men kun hvis løb er i gang...
            }
            else {
                Log.d(LOGTAG, "- onBackPressed - popBackStack()...");
                getSupportFragmentManager().popBackStack();
            }
        }


        else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Log.d(LOGTAG, "- onBackPressed - getBackStackEntryCount == 0...");
            this.finish();
        }
        else {
                Log.d(LOGTAG, "- onBackPressed - popBackStack()...");
            getSupportFragmentManager().popBackStack();
        }
    }

    private void visMainMenuFragment(boolean isSavedInstanceStateNull) {
        Log.d(LOGTAG, ":: i visMainMenuFragment.");

        if(isSavedInstanceStateNull)
        {
            android.support.v4.app.FragmentManager supportFragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frameLayoutContent, new FragmentForside());
            fragmentTransaction.commit();
        }
        else
        {
            // todo jan 16/11-15: test af pop stack
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStackImmediate();     // hvad hvis vi er på hoved menuen??
        }

        // todo jan: Husk der skal laves et tjek for: Fragment == null. ellers skal der ikke new'es.
        //

    }

    public void buttonHandlerLoeb(View view) {

        Log.d(LOGTAG, ":: i buttonHandlerLoeb.");

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fff = fragmentManager.findFragmentByTag(fragmentLoebTag);
        if(fff != null)
        {
            Log.d(LOGTAG, ":: i buttonHandlerLoeb: Fragment with tag loeb er fundet!!! **************");
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentLoeb());
        fragmentTransaction.addToBackStack(fragmentLoebTag);
        fragmentTransaction.commit();
    }

    public void buttonHandlerOm(View view) {
        
        Log.d(LOGTAG, ":: i buttonHandlerOm.");
        // todo jan - ændres hvis Singleton ikke skal new'es.
        singletonDtuRunner.buildDate = BuildInfo.GetBuildDate(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentAbout());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void buttonHandlerTestLocation(View view) {

        // todo jan 16/11-15: denne skal formentlig udgå igen!!!

        FragmentShowOnMap fragmentShowOnMap = new FragmentShowOnMap();
        FragmentShowOnMap2 fragmentShowOnMap2 = new FragmentShowOnMap2();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.add(R.id.frameLayoutContent, frag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentShowOnMap2());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // denne skulle implementeres ifb. med ...
    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(LOGTAG, ":: i onFragmentInteraction." + uri.getFragment());

    }

    public void afslutLoebButtonHandler(View view) {
        // todo jan 21/11 - denne skal ikke være her!
        visMainMenuFragment(false);
    }

    public void buttonHandlerPersonInfo(View view) {
        Log.d(LOGTAG, ":: i buttonHandlerPersonInfo.");

        visPersonInfo();
    }

    private void visPersonInfo() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentPersonInfo());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void buttonHandlerLoebsHistorik(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentLoebsHistorik());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
