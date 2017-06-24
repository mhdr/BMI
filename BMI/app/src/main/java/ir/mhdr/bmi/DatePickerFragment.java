package ir.mhdr.bmi;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import java.util.Locale;

import ir.pupli.jalalicalendarlib.JCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {

    AppCompatTextView textViewDatePicker;
    NumberPicker numberPickerDay;
    NumberPicker numberPickerMonth;
    NumberPicker numberPickerYear;
    AppCompatButton buttonDatePickerOk;
    AppCompatButton buttonDatePickerCancel;

    DatePickerFragment.OnSaveListener onSaveListener;

    String[] months;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);

        textViewDatePicker = (AppCompatTextView) view.findViewById(R.id.textViewDatePicker);
        numberPickerDay = (NumberPicker) view.findViewById(R.id.numberPickerDay);
        numberPickerMonth = (NumberPicker) view.findViewById(R.id.numberPickerMonth);
        numberPickerYear = (NumberPicker) view.findViewById(R.id.numberPickerYear);
        buttonDatePickerOk = (AppCompatButton) view.findViewById(R.id.buttonDatePickerOk);
        buttonDatePickerCancel = (AppCompatButton) view.findViewById(R.id.buttonDatePickerCancel);


        numberPickerYear.setMaxValue(1450);
        numberPickerYear.setMinValue(1300);
        numberPickerYear.setValue(1364);
        numberPickerYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        months = getContext().getResources().getStringArray(R.array.months);
        numberPickerMonth.setMinValue(1);
        numberPickerMonth.setMaxValue(12);
        numberPickerMonth.setValue(3);
        numberPickerMonth.setDisplayedValues(months);
        numberPickerMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPickerDay.setMinValue(1);
        numberPickerDay.setMaxValue(30);
        numberPickerDay.setValue(1);
        numberPickerDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPickerYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                calculate();
            }
        });

        numberPickerMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                calculate();
            }
        });

        numberPickerDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                calculate();
            }
        });

        buttonDatePickerOk.setOnClickListener(buttonDatePickerOk_OnClickListener);
        buttonDatePickerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Bundle bundle = getArguments();

        if (bundle!=null)
        {
            if (bundle.containsKey("initDate")) {
                String initDate = bundle.getString("initDate");

                if (initDate.length() > 0) {

                    String[] split = initDate.split("/");

                    int year = Integer.parseInt(split[0]);
                    int month = Integer.parseInt(split[1]);
                    int day = Integer.parseInt(split[2]);

                    numberPickerYear.setValue(year);
                    numberPickerMonth.setValue(month);
                    numberPickerDay.setValue(day);
                }
            }
        }

        calculate();

        return view;
    }

    private void calculate() {
        int year = numberPickerYear.getValue();
        int month = numberPickerMonth.getValue();
        int day = numberPickerDay.getValue();

        JCalendar jCalendar = new JCalendar(year, month, day);

        if (month == 1 || month == 2 || month == 3 || month == 4 || month == 5 || month == 6) {
            numberPickerDay.setMaxValue(31);
        } else if (month == 7 || month == 8 || month == 9 || month == 10 || month == 11) {
            numberPickerDay.setMaxValue(30);
        } else if (month == 12) {
            if (jCalendar.isLeap()) {
                numberPickerDay.setMaxValue(30);
            } else {
                numberPickerDay.setMaxValue(29);
            }
        }

        year = numberPickerYear.getValue();
        month = numberPickerMonth.getValue();
        day = numberPickerDay.getValue();

        jCalendar = new JCalendar(year, month, day);

        String dayStr = jCalendar.getDayOfWeekString();
        String monthStr = jCalendar.getMonthString();

        String dateStr = String.format(Locale.US, "%s %d %s %d", dayStr, day, monthStr, year);
        textViewDatePicker.setText(dateStr);
    }

    View.OnClickListener buttonDatePickerOk_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int year = numberPickerYear.getValue();
            int month = numberPickerMonth.getValue();
            int day = numberPickerDay.getValue();

            JCalendar jCalendar = new JCalendar(year, month, day);

            String value = jCalendar.toString();

            if (onSaveListener != null) {
                onSaveListener.onSave(value);
            }

            dismiss();
        }
    };


    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setOnSaveListener(DatePickerFragment.OnSaveListener mListener) {
        this.onSaveListener = mListener;
    }

    public interface OnSaveListener {
        void onSave(String value);
    }
}
