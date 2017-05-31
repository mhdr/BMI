package ir.mhdr.bmi;

import android.content.Context;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewProfileActivity extends AppCompatActivity {

    Toolbar toolbarNewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        toolbarNewProfile = (Toolbar) findViewById(R.id.toolbarNewProfile);
        setSupportActionBar(toolbarNewProfile);
        ViewCompat.setLayoutDirection(toolbarNewProfile, ViewCompat.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setTitle(R.string.new_profile);
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
