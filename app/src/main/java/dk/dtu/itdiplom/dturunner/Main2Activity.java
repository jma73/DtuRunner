package dk.dtu.itdiplom.dturunner;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import dk.dtu.itdiplom.dturunner.Views.FragmentAbout;
import dk.dtu.itdiplom.dturunner.Views.FragmentForside;
import dk.dtu.itdiplom.dturunner.Views.FragmentPersonInfo;

public class Main2Activity extends AppCompatActivity implements FragmentAbout.OnFragmentInteractionListener
{
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

        if(savedInstanceState == null)
        {
            Log.d(LOGTAG, "...savedInstanceState == null...");

            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frameLayoutContent, new FragmentForside());
            fragmentTransaction.commit();
        }

        // todo jan - dette er blot en test af versioner
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
        {
            Log.d(LOGTAG, "hmmm. det er en ældre version... ");
        }

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

        menu.add(0, 10, 0, "Indstillinger");
        //menu.add(0, 102, 0, "*Vælg fil");
        //menu.add(0, 110, 0, "Indtast personoplysninger");
        menu.add(0, 200, 0, "Til hovedskærm...");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOGTAG, " in onOptionsItemSelected " + item.getItemId());
        if (item.getItemId() == 200) {
            visMainMenuFragment();
        }
        else if (item.getItemId() == 110) {
//            visMainMenuFragment(false);
            visPersonInfo();
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
        En foreløbig override af onBackPressed. Dette for at stoppe bruge fra at gå ud af løbssiden ned tilbage knappen, uden først at stoppe løbet.
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

            if(fragmentBackStackName != null &&  fragmentBackStackName == SingletonDtuRunner.fragmentLoebTag)
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

    private void visMainMenuFragment() {

        Log.d(LOGTAG, ":: i visMainMenuFragment.");

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate();
    }

    // denne skulle implementeres ifb. med FragmentAbout.OnFragmentInteractionListener.
    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(LOGTAG, ":: i onFragmentInteraction." + uri.getFragment());
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


}
