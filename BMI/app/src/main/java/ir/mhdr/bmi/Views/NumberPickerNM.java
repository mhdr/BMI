package ir.mhdr.bmi.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class NumberPickerNM extends NumberPicker {
    public NumberPickerNM(Context context) {
        super(context);
    }

    public NumberPickerNM(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerNM(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);

        updateView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);

        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        updateView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);

        updateView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);

        updateView(child);
    }

    private void updateView(View view) {

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/BYekan.ttf");

        if (view instanceof EditText) {
            ((EditText) view).setTypeface(typeface);
        } else if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }
    }
}
