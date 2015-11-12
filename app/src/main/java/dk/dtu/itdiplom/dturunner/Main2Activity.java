package dk.dtu.itdiplom.dturunner;

//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import dk.dtu.itdiplom.dturunner.Utils.BuildInfo;

public class Main2Activity extends android.support.v4.app.FragmentActivity implements FragmentAbout.OnFragmentInteractionListener
{
    // Global variables. skal placeres i singleton klasse???
    static String buildDate;

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private static final String LOGTAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

//        // Setup database:
//        PointInfoDbContractDatabase database = new PointInfoDbContractDatabase(this);
//        database.getReadableDatabase();


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setVisibility(View.GONE);   // todo jan - toolbar, den skal bare fjernes!!! 1/11-2015
        //setSupportActionBar(toolbar);


        // todo jan: skal tjekke på savedInstanceState
        //  if (savedInstanceState == null) {
        //  MitFragment_frag fragment = new MitFragment_frag();

        visMainMenuFragment(savedInstanceState == null);

//        if (savedInstanceState == null) {
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.add(R.id.frameLayoutContent, new FragmentForside());
//            fragmentTransaction.commit();
//        }


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
            visMainMenuFragment(false);
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


    private void visMainMenuFragment(boolean isSavedInstanceStateNull) {
        Log.d(LOGTAG, ":: i visMainMenuFragment.");

        if(isSavedInstanceStateNull)
        {
            android.support.v4.app.FragmentManager supportFragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frameLayoutContent, new FragmentForside());
            fragmentTransaction.commit();
        }

        // todo jan: Husk der skal laves et tjek for: Fragment == null. ellers skal der ikke new'es.
        //

    }

    public void buttonHandlerLoeb2(View view) {

        Log.d(LOGTAG, ":: i buttonHandlerLoeb2.");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentLoeb());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void buttonHandlerOm(View view) {
        
        Log.d(LOGTAG, ":: i buttonHandlerOm.");
        buildDate = BuildInfo.GetBuildDate(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayoutContent, new FragmentAbout());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // denne skulle implementeres ifb. med ...
    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(LOGTAG, ":: i onFragmentInteraction.");

    }

    public void buttonHandlerAboutOk(View view) {
        visMainMenuFragment(false);
    }

    public void afslutLoebButtonHandler(View view) {
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
        fragmentTransaction.addToBackStack("TestJJ");
        fragmentTransaction.commit();
    }
}
