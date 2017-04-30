package ir.mhdr.bmi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.NumberPicker;

import ir.mhdr.bmi.Views.NumberPickerNM;
import ir.mhdr.bmi.Views.TextViewNM;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSaveListener} interface
 * to handle interaction events.
 */
public class HeightFragment extends DialogFragment {

    NumberPickerNM numberPickerHeight;
    TextViewNM textViewHeightUnit;

    public HeightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_height, container, false);

        numberPickerHeight= (NumberPickerNM) view.findViewById(R.id.numberPickerHeight);
        textViewHeightUnit= (TextViewNM) view.findViewById(R.id.textViewHeightUnit);

        numberPickerHeight.setMaxValue(999);
        numberPickerHeight.setMinValue(0);
        numberPickerHeight.setValue(170);
        numberPickerHeight.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void setOnSaveListener(OnSaveListener mListener) {
        if (mListener != null) {
            mListener.onSave();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSaveListener {
        // TODO: Update argument type and name
        void onSave();
    }
}
