package fujitsu.vidhayak.vidhayakjee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import fujitsu.vidhayak.vidhayakjee.CamearPackage.UplaodRequest;
import fujitsu.vidhayak.vidhayakjee.CamearPackage.UploadStory;
import fujitsu.vidhayak.vidhayakjee.Fragments.PendingRequest;
import fujitsu.vidhayak.vidhayakjee.Fragments.PendingStory;
import fujitsu.vidhayak.vidhayakjee.Fragments.YourFragments;
import fujitsu.vidhayak.vidhayakjee.Fragments.YourRequest;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    TextView mnametext;
    UserSessionManager session;

    String name;
  //  DashboardFragment fragment1;
    int id;
    Bundle bundle;


    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new UserSessionManager(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View hView = navigationView.getHeaderView(0);
        mnametext = (TextView) hView.findViewById(R.id.nametext);

        navigationView.setNavigationItemSelectedListener(this);


        Toast.makeText(getApplicationContext(),
                "User Login Status: " + session.isUserLoggedIn(),
                Toast.LENGTH_LONG).show();

        if (session.checkLogin())
            finish();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // get name
        name = user.get(UserSessionManager.KEY_NAME);

        mnametext.setText(name);


        tabdesign tabfrag = new tabdesign();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frame_trans, tabfrag).addToBackStack("Dashboard").commit();
        mFragmentManager.addOnBackStackChangedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if (id == R.id.logout) {

            session.logoutUser();
            DashBoard.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            tabdesign tabfrag = new tabdesign();
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.frame_trans, tabfrag).addToBackStack("Dashboard").commit();
            mFragmentManager.addOnBackStackChangedListener(this);

        } else if (id == R.id.nav_uploadrequest) {

                 Intent requstintent = new Intent(DashBoard.this, UplaodRequest.class);
                 startActivity(requstintent);


        } else if (id == R.id.nav_uploadstory) {

            Intent requstintent = new Intent(DashBoard.this, UploadStory.class);
            startActivity(requstintent);

        } else if (id == R.id.nav_completedrequest) {

            YourRequest fragment = new YourRequest();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frame_trans, fragment).addToBackStack("Your Request").commit();
            manager.addOnBackStackChangedListener(this);

        } else if (id == R.id.nav_pendingrequest) {

            PendingRequest fragment = new PendingRequest();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frame_trans, fragment).addToBackStack("Pending Request").commit();
            manager.addOnBackStackChangedListener(this);

        } else if (id == R.id.nav_completedstory) {

            YourFragments fragment = new YourFragments();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frame_trans, fragment).addToBackStack("Your Story").commit();
            manager.addOnBackStackChangedListener(this);

        }
        else if (id == R.id.nav_pendingstory) {

            PendingStory fragment = new PendingStory();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frame_trans, fragment).addToBackStack("Pending Story").commit();
            manager.addOnBackStackChangedListener(this);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackStackChanged() {

        try {

            int lastBackStackEntryCount = getSupportFragmentManager().getBackStackEntryCount() - 1;

            FragmentManager.BackStackEntry lastBackStackEntry =
                    getSupportFragmentManager().getBackStackEntryAt(lastBackStackEntryCount);

            setTitle(lastBackStackEntry.getName());

        } catch (Exception e) {

            DashBoard.this.finish();

        }

    }
}
