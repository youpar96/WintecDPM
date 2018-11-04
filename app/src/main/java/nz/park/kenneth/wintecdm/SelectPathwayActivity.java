package nz.park.kenneth.wintecdm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SelectPathwayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    CardView cvSoftware, cvDatabase, cvNetworking, cvWebDevelopment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pathway_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // check whether to change the header view and menu itmes - changed in menu itmes area
        //navigationView.inflateHeaderView(R.layout.activity_dialog););
        navigationView.setNavigationItemSelectedListener(this);

        Intent i = getIntent();
        String userType = i.getStringExtra("userType");


        // display the disclaimer to students
        if ("S".equals(userType)) {
            Intent it = new Intent(getApplicationContext(), DialogActivity.class);
            startActivity(it);
        }

        cvSoftware = findViewById(R.id.cvSoftware);
        cvDatabase = findViewById(R.id.cvDatabase);
        cvNetworking = findViewById(R.id.cvNetworking);
        cvWebDevelopment = findViewById(R.id.cvWebDevelopment);

        cvSoftware.setOnClickListener(this);
        cvDatabase.setOnClickListener(this);
        cvNetworking.setOnClickListener(this);
        cvWebDevelopment.setOnClickListener(this);
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
        // getMenuInflater().inflate(R.menu.select_pathway, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent _intent = null;
        switch (id) {
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_mypathway:
                _intent = new Intent(getApplicationContext(), MyPathway.class);
                break;
            case R.id.nav_manage:
                break;

        }

        startActivity(_intent);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, DetailPathway.class);
        String pathWay = null;

        switch (view.getId()) {
            case R.id.cvSoftware:
                pathWay = "S";
                break;
            case R.id.cvDatabase:
                pathWay = "D";
                break;
            case R.id.cvNetworking:
                pathWay = "N";
                break;
            case R.id.cvWebDevelopment:
                pathWay = "W";
                break;
            default:
                break;
        }

        i.putExtra("pathWay", pathWay);
        startActivity(i);
    }
}
