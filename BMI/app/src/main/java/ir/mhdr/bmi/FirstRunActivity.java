package ir.mhdr.bmi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.time4j.PlainDate;

import org.joda.time.DateTime;

import java.util.List;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import ir.mhdr.bmi.bl.HistoryBL;
import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.lib.Gender;
import ir.mhdr.bmi.lib.Resources;
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FirstRunActivity extends AppCompatActivity {

    List<String> genderList;
    EditText editTextProfileName;
    Spinner spinnerGender;
    ArrayAdapter<String> spinnerAdapter;
    EditText editTextBirthdate;
    Toolbar toolbarFirstRun;
    EditText editTextHeight;
    EditText editTextWeight;
    TextView textViewProfileName;
    Button buttonStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        toolbarFirstRun = (Toolbar) findViewById(R.id.toolbarFirstRun);
        setSupportActionBar(toolbarFirstRun);
        ViewCompat.setLayoutDirection(toolbarFirstRun, ViewCompat.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setTitle(R.string.initial_info);

        textViewProfileName = (TextView) findViewById(R.id.textViewProfileName);
        textViewProfileName.requestFocusFromTouch();

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

        editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        editTextWeight.setOnFocusChangeListener(editTextWeight_OnFocusChangeListener);
        editTextWeight.setOnClickListener(editTextWeight_OnClickListener);

        initializeButtonStart();
    }

    private void initializeButtonStart() {
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(buttonStart_OnClickListener);
    }

    View.OnClickListener buttonStart_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int validationError = 0;

            String name = editTextProfileName.getText().toString();

            if (name.length() == 0) {
                validationError++;
            }

            String genderStr = (String) spinnerGender.getSelectedItem();

            if (genderStr.length() == 0) {
                validationError++;
            }

            String birthdateStr = editTextBirthdate.getText().toString();

            if (birthdateStr.length() == 0) {
                validationError++;
            }

            String height = editTextHeight.getText().toString();

            if (height.length() == 0) {
                validationError++;
            }

            String weight = editTextWeight.getText().toString();

            if (weight.length() == 0) {
                validationError++;
            }

            if (validationError > 0) {
                Toast.makeText(getApplicationContext(), R.string.first_run_validation_msg, Toast.LENGTH_LONG).show();

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

            net.time4j.calendar.PersianCalendar persianCalendar = net.time4j.calendar.PersianCalendar.of(year, month, day);
            PlainDate birthdate = persianCalendar.transform(PlainDate.class);

            User user = new User();
            user.setName(name);
            user.setGenderX(gender);
            user.setBirthdate(birthdate.toString());
            user.setLatestHeight(height);
            user.setLatestWeight(weight);
            user.setIsActiveX(true);

            UserBL userBL = new UserBL(FirstRunActivity.this);
            long id = userBL.insert(user);


            if (id > 0) {

                HistoryBL historyBL = new HistoryBL(FirstRunActivity.this);

                DateTime current = new DateTime();

                History history = new History();
                history.setUserId(id);
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

    private void openPersianDatePickerDialog() {

        String previousStr = editTextBirthdate.getText().toString();
        PersianCalendar initDate = null;

        if (previousStr.length() > 0) {
            String[] previous = previousStr.split("/");
            PersianCalendar previousBirthDate = new PersianCalendar();

            int year = Integer.parseInt(previous[0]);
            int month = Integer.parseInt(previous[1]);
            int day = Integer.parseInt(previous[2]);

            previousBirthDate.setPersianDate(year, month, day);
            initDate = previousBirthDate;
        } else {
            initDate = new PersianCalendar();
            initDate.setPersianDate(1364, 3, 1);
        }

        PersianDatePickerDialog dialog = new PersianDatePickerDialog(FirstRunActivity.this)
                .setPositiveButtonString("تائید")
                .setNegativeButton("انصراف")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMaxYear(1450)
                .setMinYear(1300)
                .setInitDate(initDate)
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

    private void openHeightDialog() {
        String valueStr = editTextHeight.getText().toString();

        HeightFragment heightFragment = new HeightFragment();

        if (valueStr.length() > 0) {
            heightFragment.setHeightValue(Integer.parseInt(valueStr));
        }

        heightFragment.setOnSaveListener(new HeightFragment.OnSaveListener() {
            @Override
            public void onSave(int value) {
                editTextHeight.setText(String.valueOf(value));
            }
        });

        heightFragment.show(getSupportFragmentManager(), "height");
    }

    private void openWeightDialog() {
        String valueStr = editTextWeight.getText().toString();

        WeightFragment weightFragment = new WeightFragment();

        if (valueStr.length() > 0) {
            weightFragment.setWeightValue(Double.parseDouble(valueStr));
        }

        weightFragment.setOnSaveListener(new WeightFragment.OnSaveListener() {
            @Override
            public void onSave(double value) {
                editTextWeight.setText(String.valueOf(value));
            }
        });
        weightFragment.show(getSupportFragmentManager(), "weight");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}