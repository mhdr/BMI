package ir.mhdr.bmi.lib;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.Locale;

import ir.mhdr.bmi.R;

public class CustomMarkerView extends MarkerView {

    AppCompatTextView textViewMarkerWeightValue;
    AppCompatTextView textViewMarkerDateValue;
    AppCompatTextView textViewMarkerWeightLabel;
    AppCompatTextView textViewMarkerDateLabel;
    MPPointF mOffset;
    RelativeLayout relativeLayoutMarkerView;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        textViewMarkerDateValue = (AppCompatTextView) findViewById(R.id.textViewMarkerDateValue);
        textViewMarkerWeightValue = (AppCompatTextView) findViewById(R.id.textViewMarkerWeightValue);
        textViewMarkerWeightLabel = (AppCompatTextView) findViewById(R.id.textViewMarkerWeightLabel);
        textViewMarkerDateLabel = (AppCompatTextView) findViewById(R.id.textViewMarkerDateLabel);
        relativeLayoutMarkerView = (RelativeLayout) findViewById(R.id.relativeLayoutMarkerView);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        // update

        textViewMarkerWeightValue.setText(String.valueOf(e.getY()));

        TimeDiff timeDiff = TimeDiff.fromMinutes((int) e.getX());
        int year = timeDiff.getPersianCalendar().getYear();
        int month = timeDiff.getPersianCalendar().getMonth().getValue();
        int day = timeDiff.getPersianCalendar().getDayOfMonth();
        int hour=timeDiff.getDateTime().getHourOfDay();
        int minute=timeDiff.getDateTime().getMinuteOfHour();
        String date = String.format(Locale.US,"%d/%d/%d %d:%d", year, month, day,hour,minute);

        textViewMarkerDateValue.setText(date);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
