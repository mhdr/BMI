package ir.mhdr.bmi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
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

        View view=inflater.inflate(R.layout.fragment_weight_range, container, false);

        textViewWeightRange1 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange1);
        textViewWeightRange2 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange2);
        textViewWeightRange3 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange3);
        textViewWeightRange4 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange4);
        textViewWeightRange5 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange5);
        textViewWeightRange6 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange6);
        textViewWeightRange7 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange7);
        textViewWeightRange8 = (AppCompatTextView) view.findViewById(R.id.textViewWeightRange8);

        return view;
    }

}
