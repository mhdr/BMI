package ir.mhdr.bmi;

import android.content.Context;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbarEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbarEditProfile = (Toolbar) findViewById(R.id.toolbarEditProfile);
        setSupportActionBar(toolbarEditProfile);
        ViewCompat.setLayoutDirection(toolbarEditProfile, ViewCompat.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setTitle(R.string.edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
