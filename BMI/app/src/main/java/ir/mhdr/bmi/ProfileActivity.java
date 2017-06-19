package ir.mhdr.bmi;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.lib.FirebaseUtils;
import ir.mhdr.bmi.lib.ProfileAdapter;
import ir.mhdr.bmi.model.User;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbarProfile;
    FloatingActionButton floatingActionButtonNewProfile;
    RecyclerView recyclerViewProfiles;
    private ProfileAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (FirebaseUtils.checkPlayServices(this)) {
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        }

        toolbarProfile = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbarProfile);
        getSupportActionBar().setTitle(R.string.profile_management);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        floatingActionButtonNewProfile = (FloatingActionButton) findViewById(R.id.floatingActionButtonNewProfile);
        floatingActionButtonNewProfile.setOnClickListener(floatingActionButtonNewProfile_OnClickListener);

        recyclerViewProfiles = (RecyclerView) findViewById(R.id.recyclerViewProfiles);

        layoutManager = new LinearLayoutManager(ProfileActivity.this);
        recyclerViewProfiles.setLayoutManager(layoutManager);

        recyclerViewProfiles.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new ProfileAdapter(new ArrayList<User>());
        recyclerViewProfiles.setAdapter(adapter);
    }

    View.OnClickListener floatingActionButtonNewProfile_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(ProfileActivity.this,NewEditProfileActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        UserBL userBL = new UserBL(this);
        List<User> userList = userBL.getUsers();
        adapter.setUserList(userList);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                //Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
                //NavUtils.navigateUpTo(ProfileActivity.this,intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        NavUtils.navigateUpFromSameTask(this);

        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
