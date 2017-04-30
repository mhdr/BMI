package ir.mhdr.bmi;

import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.List;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import ir.mhdr.bmi.lib.Resources;

public class FirstRunActivity extends AppCompatActivity {

    List<String> genderList;
    EditText editTextProfileName;
    Spinner spinnerGender;
    ArrayAdapter<String> spinnerAdapter;
    EditText editTextBirthdate;
    Toolbar toolbarFirstRun;
    EditText editTextHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        toolbarFirstRun = (Toolbar) findViewById(R.id.toolbarFirstRun);
        setSupportActionBar(toolbarFirstRun);
        ViewCompat.setLayoutDirection(toolbarFirstRun, ViewCompat.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setTitle(R.string.first_run);

        editTextProfileName = (EditText) findViewById(R.id.editTextProfileName);
        ViewCompat.setLayoutDirection(editTextProfileName, ViewCompat.LAYOUT_DIRECTION_RTL);

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);

        Resources resources = new Resources(FirstRunActivity.this);
        genderList = resources.getGenderList();


        spinnerAdapter = new ArrayAdapter<String>(FirstRunActivity.this, android.R.layout.simple_spinner_item, genderList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(spinnerAdapter);
        ViewCompat.setLayoutDirection(spinnerGender, ViewCompat.LAYOUT_DIRECTION_RTL);

        editTextBirthdate = (EditText) findViewById(R.id.editTextBirthdate);
        editTextBirthdate.setOnFocusChangeListener(editTextBirthdate_OnFocusChangeListener);
        editTextBirthdate.setOnClickListener(editTextBirthdate_OnClickListener);

        editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        editTextHeight.setOnFocusChangeListener(editTextHeight_OnFocusChangeListener);
        editTextHeight.setOnClickListener(editTextHeight_OnClickListener);
    }

    View.OnFocusChangeListener editTextBirthdate_OnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                openPersianDatePickerDialog();
            }

        }
    };

    View.OnClickListener editTextBirthdate_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openPersianDatePickerDialog();
        }
    };

    View.OnFocusChangeListener editTextHeight_OnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                openHeightDialog();
            }

        }
    };

    View.OnClickListener editTextHeight_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openHeightDialog();
        }
    };

    private void openPersianDatePickerDialog() {
        PersianDatePickerDialog dialog = new PersianDatePickerDialog(FirstRunActivity.this)
                .setPositiveButtonString("تائید")
                .setNegativeButton("انصراف")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMaxYear(1400)
                .setMinYear(1300)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(PersianCalendar persianCalendar) {
                        int year = persianCalendar.getPersianYear();
                        int month = persianCalendar.getPersianMonth();
                        int day = persianCalendar.getPersianDay();

                        String output = String.format("%s/%s/%s", year, month, day);
                        editTextBirthdate.setText(output);
                    }

                    @Override
                    public void onDisimised() {

                    }
                });

        dialog.show();
    }

    private void openHeightDialog()
    {
        HeightFragment heightFragment=new HeightFragment();
        heightFragment.setOnSaveListener(new HeightFragment.OnSaveListener() {
            @Override
            public void onSave() {

            }
        });

        heightFragment.show(getSupportFragmentManager(),"height1");
    }
}