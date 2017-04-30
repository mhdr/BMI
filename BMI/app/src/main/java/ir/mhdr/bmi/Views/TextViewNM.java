package ir.mhdr.bmi.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ir.mhdr.bmi.R;

public class TextViewNM extends android.support.v7.widget.AppCompatTextView {
    public TextViewNM(Context context) {
        super(context);

        updateView();
    }

    public TextViewNM(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        updateView();
    }

    public TextViewNM(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        updateView();
    }

    private void updateView()
    {
        Typeface customFont = Typeface.createFromAsset(getContext().getAssets(),getContext().getResources().getString(R.string.font_path));
        setTypeface(customFont);
    }
}
