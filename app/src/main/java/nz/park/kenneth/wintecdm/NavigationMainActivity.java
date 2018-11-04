package nz.park.kenneth.wintecdm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class NavigationMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // check whether to change the header view and menu itmes - changed in menu itmes area
        //navigationView.inflateHeaderView(R.layout.activity_dialog););
        navigationView.setNavigationItemSelectedListener(this);

        // Automatically load the first menu fragment
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SelectPathwayFragment()).commit();
            //navigationView.setCheckedItem(R.id.navHome);
        }

        Intent i = getIntent();
        String userType = i.getStringExtra("userType");

        // display the disclaimer to students
        if ("S".equals(userType)) {
            Intent it = new Intent(getApplicationContext(), DialogActivity.class);
            startActivity(it);
        }

        // separate menu by userType
        // create header view to get views from navigation view
        View navHeader = navigationView.getHeaderView(0);
        TextView navUserName = navHeader.findViewById(R.id.navUserName);

        // students
        if("S".equals(userType)){
            navUserName.setText("Student");

            navigationView.inflateMenu(R.menu.student_menu);
        }
        // managers (clients)
        else if("M".equals(userType)){
            navUserName.setText("Client");

            navigationView.inflateMenu(R.menu.manager_menu);
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_select_pathway:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SelectPathwayFragment()).commit();
                break;
            case R.id.nav_s_my_pathway:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyPathwayFragment()).commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
