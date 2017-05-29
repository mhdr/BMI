package ir.mhdr.bmi;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.model.User;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbarProfile;
    ListView listViewProfiles;
    FloatingActionButton floatingActionButtonNewProfile;
    ArrayAdapter<String> adapter;

    String[] profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbarProfile = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbarProfile);
        ViewCompat.setLayoutDirection(toolbarProfile, ViewCompat.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setTitle(R.string.profile_management);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listViewProfiles = (ListView) findViewById(R.id.listViewProfiles);
        floatingActionButtonNewProfile = (FloatingActionButton) findViewById(R.id.floatingActionButtonNewProfile);

        generateProfiles();

        listViewProfiles.setAdapter(adapter);
    }

    private void generateProfiles() {
        UserBL userBL = new UserBL(ProfileActivity.this);
        List<User> userList = userBL.getUsers();
        List<String> profileStrList = new ArrayList<>();

        for (User u : userList) {
            profileStrList.add(u.getName());
        }

        profiles = profileStrList.toArray(new String[0]);

        adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, profiles);
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
}
