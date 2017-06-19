package ir.mhdr.bmi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import ir.mhdr.bmi.bl.HistoryBL;
import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.lib.CustomViewPager;
import ir.mhdr.bmi.lib.FirebaseUtils;
import ir.mhdr.bmi.lib.MainViewPagerAdapter;
import ir.mhdr.bmi.lib.ProfileChangedListener;
import ir.mhdr.bmi.lib.Statics;
import ir.mhdr.bmi.lib.Update;
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    BottomNavigationView bottomNavigationView;
    CustomViewPager viewPagerMain;
    NavigationView navigationView;
    boolean firstRun = false;
    AppCompatSpinner spinnerProfile;
    ArrayAdapter<String> spinnerAdapter;

    String[] profiles;
    MainViewPagerAdapter viewPagerAdapter;

    private FirebaseAnalytics mFirebaseAnalytics;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseUtils.checkPlayServices(this)) {
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        }


        if (!Statics.isCheckedForUpdate)
        {
            //check for update just once
            Update update = new Update();
            update.setUpdateListener(updateListener);
            update.Check();
            //
        }


        UserBL userBL = new UserBL(MainActivity.this);

        if (userBL.countUsers() == 0) {
            firstRun = true;
        }

        if (firstRun) {
            Intent intent = new Intent(MainActivity.this, FirstRunActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // to close current activity
        }

        navigationView = (NavigationView) findViewById(R.id.navigationView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name_fa);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        viewPagerMain = (CustomViewPager) findViewById(R.id.viewPagerMain);
        viewPagerMain.setPagingEnabled(false);

        viewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPagerMain.setAdapter(viewPagerAdapter);

        // set selected item
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        viewPagerMain.setCurrentItem(1);

        // bind bottomNavigationView and viewPager
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationView_OnNavigationItemSelectedListener);
        viewPagerMain.addOnPageChangeListener(viewPagerMain_OnPageChangeListener);

        navigationView.setNavigationItemSelectedListener(navigationView_OnNavigationItemSelectedListener);


        View headerView = navigationView.getHeaderView(0);
        spinnerProfile = (AppCompatSpinner) headerView.findViewById(R.id.spinnerProfile);
        generateSpinnerProfile();
        spinnerProfile.setOnItemSelectedListener(spinnerProfile_OnItemSelectedListener);
    }

    AdapterView.OnItemSelectedListener spinnerProfile_OnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            UserBL userBL = new UserBL(MainActivity.this);
            userBL.SwitchActiveUser(userBL.getUsers().get(position));

            int pos = viewPagerMain.getCurrentItem();
            ProfileChangedListener profileChangedListener = (ProfileChangedListener) viewPagerAdapter.getRegisteredFragment(pos);
            profileChangedListener.onProfileChanged();

            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END); // close the navigation drawer after changing profile
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void generateSpinnerProfile() {
        UserBL userBL = new UserBL(MainActivity.this);
        List<User> userList = userBL.getUsers();
        List<String> profileStrList = new ArrayList<>();

        int activeUserIndex = 0;
        int currentIndex = 0;

        for (User u : userList) {

            profileStrList.add(u.getName());

            if (u.getIsActiveX()) {
                activeUserIndex = currentIndex;
            }

            currentIndex++;
        }

        profiles = profileStrList.toArray(new String[0]);
        spinnerAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.simple_spinner_dropdown_item_rtl, profiles);
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_rtl);
        spinnerProfile.setAdapter(spinnerAdapter);
        spinnerProfile.setSelection(activeUserIndex);

    }

    NavigationView.OnNavigationItemSelectedListener navigationView_OnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // uncheck other menus and sub menus
            int menuSize = navigationView.getMenu().size();

            for (int i = 0; i < menuSize; i++) {
                MenuItem menuItem = navigationView.getMenu().getItem(i);

                if (menuItem.hasSubMenu()) {
                    int submenuSize = menuItem.getSubMenu().size();

                    for (int j = 0; j < submenuSize; j++) {
                        MenuItem subItem = menuItem.getSubMenu().getItem(j);
                        subItem.setChecked(false);
                    }
                } else {
                    menuItem.setChecked(false);
                }
            }

            item.setChecked(true);

            if (item.getItemId() == R.id.itemMenuExit) {

                finish();
                System.exit(0);
                return true;
            } else if (item.getItemId() == R.id.itemMenuProfile) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }

            return false;
        }
    };

    BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationView_OnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.item_bn_graph:
                    viewPagerMain.setCurrentItem(0);
                    break;
                case R.id.item_bn_bmi:
                    viewPagerMain.setCurrentItem(1);
                    break;
                case R.id.item_bn_table:
                    viewPagerMain.setCurrentItem(2);
                    break;
            }

            return true;
        }
    };

    ViewPager.OnPageChangeListener viewPagerMain_OnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    break;
                case 1:
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    break;
                case 2:
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    Update.UpdateListener updateListener = new Update.UpdateListener() {
        @Override
        public void newUpdateAvailable(Update.UpdateInfo updateInfo) {

            final Update.UpdateInfo localUpdateInfo = updateInfo;
            Statics.isCheckedForUpdate=true;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConfirmUpdateFragment confirmUpdateFragment = new ConfirmUpdateFragment();
                        confirmUpdateFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                        confirmUpdateFragment.setUpdateInfo(localUpdateInfo);
                        confirmUpdateFragment.show(getSupportFragmentManager(), "confirm_update");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        FirebaseCrash.report(ex);
                    }
                }
            });

        }
    };

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        }

        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
