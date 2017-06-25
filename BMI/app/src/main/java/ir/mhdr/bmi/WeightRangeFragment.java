package ir.mhdr.bmi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import ir.mhdr.bmi.lib.BMI;


public class WeightRangeFragment extends Fragment {


    AppCompatTextView textViewWeightRange1;
    AppCompatTextView textViewWeightRange2;
    AppCompatTextView textViewWeightRange3;
    AppCompatTextView textViewWeightRange4;
    AppCompatTextView textViewWeightRange5;
    AppCompatTextView textViewWeightRange6;
    AppCompatTextView textViewWeightRange7;
    AppCompatTextView textViewWeightRange8;

    public WeightRangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weight_range, container, false);

        textViewWeightRange1 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange1);
        textViewWeightRange2 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange2);
        textViewWeightRange3 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange3);
        textViewWeightRange4 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange4);
        textViewWeightRange5 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange5);
        textViewWeightRange6 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange6);
        textViewWeightRange7 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange7);
        textViewWeightRange8 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange8);

        Bundle bundle = getArguments();

        BMI bmi = (BMI) bundle.getSerializable("bmi");

        if (bmi != null) {
            textViewWeightRange1.setText(String.format(Locale.US, "کمتر از %.2f کیلوگرم", bmi.weightPoint1()));
            textViewWeightRange2.setText(String.format(Locale.US, "%.2f - %.2f کیلوگرم", bmi.weightPoint1(), bmi.weightPoint2()));
            textViewWeightRange3.setText(String.format(Locale.US, "%.2f - %.2f کیلوگرم", bmi.weightPoint2(), bmi.weightPoint3()));
            textViewWeightRange4.setText(String.format(Locale.US, "%.2f - %.2f کیلوگرم", bmi.weightPoint3(), bmi.weightPoint4()));
            textViewWeightRange5.setText(String.format(Locale.US, "%.2f - %.2f کیلوگرم", bmi.weightPoint4(), bmi.weightPoint5()));
            textViewWeightRange6.setText(String.format(Locale.US, "%.2f - %.2f کیلوگرم", bmi.weightPoint5(), bmi.weightPoint6()));
            textViewWeightRange7.setText(String.format(Locale.US, "%.2f - %.2f کیلوگرم", bmi.weightPoint6(), bmi.weightPoint7()));
            textViewWeightRange8.setText(String.format(Locale.US, "بیشتر از %.2f کیلوگرم", bmi.weightPoint7()));
        }

        return view;
    }

}
