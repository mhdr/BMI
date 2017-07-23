package ir.mhdr.bmi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

import ir.mhdr.bmi.blDao.UserBL;
import ir.mhdr.bmi.dao.User;
import ir.mhdr.bmi.lib.CustomViewPager;
import ir.mhdr.bmi.lib.FirebaseUtils;
import ir.mhdr.bmi.lib.MainViewPagerAdapter;
import ir.mhdr.bmi.lib.ProfileChangedListener;
import ir.mhdr.bmi.lib.Statics;
import ir.mhdr.bmi.lib.Update;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
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
    AdapterView.OnItemSelectedListener spinnerProfile_OnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            UserBL userBL = new UserBL();
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
            } else if (item.getItemId() == R.id.itemMenuTelegram) {
                final String appName = "org.telegram.messenger";
                final boolean isAppInstalled = isAppAvailable(getApplicationContext(), appName);
                if (isAppInstalled) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/BSLSdUJkvhEVnDI4lbNw3g"));
                    startActivity(intent);
                } else {
                    String msg = getResources().getString(R.string.telegram_not_installed);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

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
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseUtils.checkPlayServices(this)) {
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            mFirebaseAnalytics.setCurrentScreen(this, "MainActivity", this.getClass().getSimpleName());
            mFirebaseAnalytics.setUserProperty(FirebaseUtils.UserProperty.InstallSource, Statics.InstallSource);
        }

        if (!Statics.isCheckedForUpdate) {
            //check for update just once
            Update update = new Update(this);
            update.setUpdateListener(updateListener);
            update.Check();
            //
        }

        UserBL userBL = new UserBL();

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
        navigationView.setItemIconTintList(null);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name_fa);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

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

    Update.UpdateListener updateListener = new Update.UpdateListener() {
        @Override
        public void newUpdateAvailable(Update.UpdateInfo updateInfo) {

            final Update.UpdateInfo localUpdateInfo = updateInfo;
            Statics.isCheckedForUpdate = true;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ConfirmUpdateFragment confirmUpdateFragment = new ConfirmUpdateFragment();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            confirmUpdateFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                        }

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

    private void generateSpinnerProfile() {
        UserBL userBL = new UserBL();
        List<User> userList = userBL.getUsers();
        List<String> profileStrList = new ArrayList<>();

        int activeUserIndex = 0;
        int currentIndex = 0;

        for (User u : userList) {

            profileStrList.add(u.getName());

            if (u.isActive()) {
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

    public boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
