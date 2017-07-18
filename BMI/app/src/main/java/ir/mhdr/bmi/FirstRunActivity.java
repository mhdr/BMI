package ir.mhdr.bmi;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Locale;

import ir.mhdr.bmi.blDao.HistoryBL;
import ir.mhdr.bmi.blDao.PrivateSettingBL;
import ir.mhdr.bmi.blDao.UserBL;
import ir.mhdr.bmi.dao.History;
import ir.mhdr.bmi.dao.PrivateSetting;
import ir.mhdr.bmi.dao.User;
import ir.mhdr.bmi.lib.FirebaseUtils;
import ir.mhdr.bmi.lib.Gender;
import ir.mhdr.bmi.lib.Resources;
import ir.mhdr.bmi.lib.Statics;
import ir.pupli.jalalicalendarlib.JCalendar;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class FirstRunActivity extends AppCompatActivity {

    List<String> genderList;
    AppCompatEditText editTextProfileName;
    AppCompatSpinner spinnerGender;
    ArrayAdapter<String> spinnerAdapter;
    AppCompatEditText editTextBirthdate;
    Toolbar toolbarFirstRun;
    AppCompatEditText editTextHeight;
    AppCompatEditText editTextWeight;
    AppCompatTextView textViewProfileName;
    AppCompatButton buttonStart;
    View.OnClickListener buttonStart_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int validationError = 0;

            String name = editTextProfileName.getText().toString();

            if (name.length() == 0) {
                validationError++;
                editTextProfileName.setError(getResources().getString(R.string.validate_profile_name));
            }

            String genderStr = (String) spinnerGender.getSelectedItem();

            if (genderStr.length() == 0) {
                validationError++;
            }

            String birthdateStr = editTextBirthdate.getText().toString();

            if (birthdateStr.length() == 0) {
                validationError++;
                editTextBirthdate.setError(getResources().getString(R.string.validate_birthdate));
            }

            String height = editTextHeight.getText().toString();

            if (height.length() == 0) {
                validationError++;
                editTextHeight.setError(getResources().getString(R.string.validate_height));
            }

            String weight = editTextWeight.getText().toString();

            if (weight.length() == 0) {
                validationError++;
                editTextWeight.setError(getResources().getString(R.string.validate_weight));
            }

            if (validationError > 0) {
                Toast.makeText(getApplicationContext(), R.string.new_profile_validation_msg, Toast.LENGTH_LONG).show();

                return;
            }

            Gender gender = Gender.Male;

            switch (genderStr) {
                case "مرد":
                    gender = Gender.Male;
                    break;
                case "زن":
                    gender = Gender.Female;
                    break;
            }

            String[] birthdateArray = birthdateStr.split("/");
            int year = Integer.parseInt(birthdateArray[0]);
            int month = Integer.parseInt(birthdateArray[1]);
            int day = Integer.parseInt(birthdateArray[2]);

            JCalendar jCalendar = new JCalendar(year, month, day);
            DateTime birthdate = new DateTime(jCalendar.toGregorianDate());

            User user = new User();
            user.setName(name);
            user.setGenderX(gender);
            user.setBirthdate(birthdate.toString());
            user.setLatestHeight(height);
            user.setLatestWeight(weight);

            UserBL userBL = new UserBL();
            long id = userBL.insert(user);

            PrivateSettingBL privateSettingBL = new PrivateSettingBL();
            privateSettingBL.setActiveUser(user);


            if (id > 0) {

                HistoryBL historyBL = new HistoryBL();

                DateTime current = new DateTime();

                History history = new History();
                history.setUserUuid(user.getUuid());
                history.setValue(weight);
                history.setDatetime(current.toString());

                long historyId = historyBL.insert(history);

                if (historyId > 0) {
                    Toast.makeText(getApplicationContext(), R.string.profile_created_successful_msg, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(FirstRunActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }
    };
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
    View.OnFocusChangeListener editTextWeight_OnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                openWeightDialog();
            }
        }
    };
    View.OnClickListener editTextWeight_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openWeightDialog();
        }
    };
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        if (FirebaseUtils.checkPlayServices(this)) {
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            mFirebaseAnalytics.setCurrentScreen(this, "FirstRunActivity", this.getClass().getSimpleName());
            mFirebaseAnalytics.setUserProperty(FirebaseUtils.UserProperty.InstallSource, Statics.InstallSource);
        }

        toolbarFirstRun = (Toolbar) findViewById(R.id.toolbarFirstRun);
        setSupportActionBar(toolbarFirstRun);
        getSupportActionBar().setTitle(R.string.initial_info);

        textViewProfileName = (AppCompatTextView) findViewById(R.id.textViewProfileName);

        textViewProfileName.setFocusable(true);
        textViewProfileName.setFocusableInTouchMode(true);
        textViewProfileName.requestFocusFromTouch();


        editTextProfileName = (AppCompatEditText) findViewById(R.id.editTextProfileName);

        spinnerGender = (AppCompatSpinner) findViewById(R.id.spinnerGender);

        Resources resources = new Resources(FirstRunActivity.this);
        genderList = resources.getGenderList();


        spinnerAdapter = new ArrayAdapter<String>(FirstRunActivity.this, R.layout.simple_spinner_dropdown_item_rtl, genderList);
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_rtl);
        spinnerGender.setAdapter(spinnerAdapter);

        editTextBirthdate = (AppCompatEditText) findViewById(R.id.editTextBirthdate);
        editTextBirthdate.setOnFocusChangeListener(editTextBirthdate_OnFocusChangeListener);
        editTextBirthdate.setOnClickListener(editTextBirthdate_OnClickListener);

        editTextHeight = (AppCompatEditText) findViewById(R.id.editTextHeight);
        editTextHeight.setOnFocusChangeListener(editTextHeight_OnFocusChangeListener);
        editTextHeight.setOnClickListener(editTextHeight_OnClickListener);

        editTextWeight = (AppCompatEditText) findViewById(R.id.editTextWeight);
        editTextWeight.setOnFocusChangeListener(editTextWeight_OnFocusChangeListener);
        editTextWeight.setOnClickListener(editTextWeight_OnClickListener);

        initializeButtonStart();

    }

    private void initializeButtonStart() {
        buttonStart = (AppCompatButton) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(buttonStart_OnClickListener);
    }

    private void openPersianDatePickerDialog() {
        String previousStr = editTextBirthdate.getText().toString();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        datePickerFragment.setOnSaveListener(new DatePickerFragment.OnSaveListener() {
            @Override
            public void onSave(String value) {
                editTextBirthdate.setText(value);
                editTextBirthdate.setError(null);
            }
        });

        if (previousStr.length() > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("initDate", previousStr);
            datePickerFragment.setArguments(bundle);
        }

        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void openHeightDialog() {
        String valueStr = editTextHeight.getText().toString();

        HeightFragment heightFragment = new HeightFragment();
        heightFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);

        if (valueStr.length() > 0) {
            heightFragment.setHeightValue(Integer.parseInt(valueStr));
        }

        heightFragment.setOnSaveListener(new HeightFragment.OnSaveListener() {
            @Override
            public void onSave(int value) {
                editTextHeight.setText(String.valueOf(value));
                editTextHeight.setError(null);
            }
        });

        heightFragment.show(getSupportFragmentManager(), "height");
    }

    private void openWeightDialog() {
        String valueStr = editTextWeight.getText().toString();

        WeightFragment weightFragment = new WeightFragment();
        weightFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);

        if (valueStr.length() > 0) {
            weightFragment.setWeightValue(Double.parseDouble(valueStr));
        }

        weightFragment.setOnSaveListener(new WeightFragment.OnSaveListener() {
            @Override
            public void onSave(double value) {
                editTextWeight.setText(String.valueOf(value));
                editTextWeight.setError(null);
            }
        });
        weightFragment.show(getSupportFragmentManager(), "weight");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}