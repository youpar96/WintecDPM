package nz.park.kenneth.wintecdm;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.TextView;

import nz.park.kenneth.wintecdm.helper.ActionPermission;

public class NavigationMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean checkDisclaimer = false;
    private final static String mainScreen = "SelectPathwayFragment";

    public void setCheckDisclaimer(boolean checkDisclaimer) {
        this.checkDisclaimer = checkDisclaimer;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("checkDisclaimer", checkDisclaimer);
    }

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
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SelectPathwayFragment()).commit();
            //navigationView.setCheckedItem(R.id.navHome);
        } else {
            // set the value of savedInstanceState when the screen rotates
            boolean disclaimer = savedInstanceState.getBoolean("checkDisclaimer");

            setCheckDisclaimer(disclaimer);
        }

        Intent i = getIntent();
        String userType = i.getStringExtra("userType");

        // display the disclaimer to students - only once
        if ("S".equals(userType)) {
            if (!checkDisclaimer) {
                Intent it = new Intent(getApplicationContext(), DialogActivity.class);
                startActivity(it);

                checkDisclaimer = true;
            }
        }

        // separate menu by userType
        // create header view to get views from navigation view
        View navHeader = navigationView.getHeaderView(0);

        ImageView navTitleImg = navHeader.findViewById(R.id.navTitleImg);
        TextView navUserName = navHeader.findViewById(R.id.navUserName);

        // students
        if ("S".equals(userType)) {
            Profile.isAdmin = false;
            navTitleImg.setImageResource(R.mipmap.icon_student);
            navUserName.setText("Student");

            navigationView.inflateMenu(R.menu.student_menu);
        }
        // managers (clients)
        else if ("M".equals(userType)) {

            Profile.isAdmin = true;
            navTitleImg.setImageResource(R.mipmap.icon_manager);
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SelectPathwayFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_s_my_pathway:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyPathwayFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_m_send_plan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SendPathwayFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_m_add_module:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InputModuleFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_s_edit_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_s_about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUs()).commit();
                break;
            case R.id.nav_m_import_export:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ImportExportFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_m_find_student:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentListFragment()).commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
