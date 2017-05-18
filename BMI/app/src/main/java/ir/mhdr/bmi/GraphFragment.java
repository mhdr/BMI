package ir.mhdr.bmi;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.AndroidCharacter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ir.mhdr.bmi.bl.HistoryBL;
import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.lib.CustomMarkerView;
import ir.mhdr.bmi.lib.DateAxisValueFormatter;
import ir.mhdr.bmi.lib.TimeDiff;
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;

public class GraphFragment extends Fragment {

    LineChart lineChartWeight;
    Button buttonGraphShowOneMonth;
    Button buttonGraphShowOneWeek;
    Button buttonGraphShowAll;
    Button buttonGraphShowOneYear;
    Button buttonGraphShowMore;

    DateTime start;
    DateTime end;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        lineChartWeight = (LineChart) view.findViewById(R.id.lineChartWeight);

        buttonGraphShowAll = (Button) view.findViewById(R.id.buttonGraphShowAll);
        buttonGraphShowMore = (Button) view.findViewById(R.id.buttonGraphShowMore);
        buttonGraphShowOneMonth = (Button) view.findViewById(R.id.buttonGraphShowOneMonth);
        buttonGraphShowOneWeek = (Button) view.findViewById(R.id.buttonGraphShowOneWeek);
        buttonGraphShowOneYear = (Button) view.findViewById(R.id.buttonGraphShowOneYear);


        buttonGraphShowAll.setOnClickListener(buttonGraphShowAll_OnClickListener);
        buttonGraphShowMore.setOnClickListener((buttonGraphShowMore_OnClickListener));
        buttonGraphShowOneMonth.setOnClickListener(buttonGraphShowOneMonth_OnClickListener);
        buttonGraphShowOneWeek.setOnClickListener(buttonGraphShowOneWeek_OnClickListener);
        buttonGraphShowOneYear.setOnClickListener(buttonGraphShowOneYear_OnClickListener);
        return view;
    }

    View.OnClickListener buttonGraphShowAll_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonGraphShowAll.setTextColor(getResources().getColor(android.R.color.black));
            buttonGraphShowMore.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneMonth.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneWeek.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneYear.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            drawChart(Period.All);
        }
    };

    View.OnClickListener buttonGraphShowMore_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonGraphShowAll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowMore.setTextColor(getResources().getColor(android.R.color.black));
            buttonGraphShowOneMonth.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneWeek.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneYear.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    };

    View.OnClickListener buttonGraphShowOneMonth_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonGraphShowAll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowMore.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneMonth.setTextColor(getResources().getColor(android.R.color.black));
            buttonGraphShowOneWeek.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneYear.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            drawChart(Period.OneMonth);
        }
    };


    View.OnClickListener buttonGraphShowOneWeek_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonGraphShowAll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowMore.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneMonth.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneWeek.setTextColor(getResources().getColor(android.R.color.black));
            buttonGraphShowOneYear.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            drawChart(Period.OneWeek);
        }
    };

    View.OnClickListener buttonGraphShowOneYear_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonGraphShowAll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowMore.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneMonth.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneWeek.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneYear.setTextColor(getResources().getColor(android.R.color.black));

            drawChart(Period.OneWeek);
        }
    };

    private void drawChart(Period period) {

        this.calculateStartAndEndTime(period);

        lineChartWeight.clear();

        List<Entry> entries = new ArrayList<Entry>();

        UserBL userBL = new UserBL(getContext());
        HistoryBL historyBL = new HistoryBL(getContext());

        User user = userBL.getActiveUser();
        List<History> historyList = historyBL.getHistory(user);

        for (History h : historyList) {
            if (period == Period.All) {
                TimeDiff timeDiff = new TimeDiff(h.getDatetime2());
                float value = timeDiff.getMinutes();

                Entry entry = new Entry(value, Float.parseFloat(h.getValue()));
                entries.add(entry);
            } else {

                DateTime dateTime = h.getDatetime2();

                if (dateTime.isAfter(start) && dateTime.isBefore(end)) {
                    TimeDiff timeDiff = new TimeDiff(h.getDatetime2());
                    float value = timeDiff.getMinutes();

                    Entry entry = new Entry(value, Float.parseFloat(h.getValue()));
                    entries.add(entry);
                }
            }
        }

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/BYekan.ttf");

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));
        dataSet.setCircleColor(getResources().getColor(R.color.colorGreenDark));
        dataSet.setLineWidth(4);
        dataSet.setHighlightEnabled(true);
        dataSet.setValueTypeface(typeface);
        dataSet.setValueTextSize(10);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        LineData lineData = new LineData(dataSet);
        lineData.setDrawValues(true);

        Description description = new Description();
        description.setText("");
        lineChartWeight.setDescription(description);
        lineChartWeight.getLegend().setEnabled(false);
        lineChartWeight.setBackgroundColor(getResources().getColor(R.color.colorBackground));
        lineChartWeight.animateX(300, Easing.EasingOption.EaseInExpo);
        lineChartWeight.setDoubleTapToZoomEnabled(false);
        lineChartWeight.setPinchZoom(false);
        lineChartWeight.setScaleYEnabled(false);
        lineChartWeight.setNoDataText(getResources().getString(R.string.no_chart_data));
        lineChartWeight.resetZoom();
        lineChartWeight.resetTracking();
        lineChartWeight.resetViewPortOffsets();
        lineChartWeight.fitScreen();

        IMarker customMarkerView = new CustomMarkerView(getContext(), R.layout.custom_marker_view_layout);
        lineChartWeight.setMarker(customMarkerView);

        XAxis xAxis = lineChartWeight.getXAxis();
        xAxis.setValueFormatter(new DateAxisValueFormatter(lineChartWeight));
        xAxis.setGranularity(1f); // restrict interval to 1 (minimum)
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(3);
        xAxis.setTypeface(typeface);

        YAxis yAxisLeft = lineChartWeight.getAxisLeft();
        yAxisLeft.setTypeface(typeface);
        //yAxisLeft.setAxisMinimum(0);
        //yAxisLeft.setAxisMaximum(average *2);

        YAxis yAxisRight = lineChartWeight.getAxisRight();
        yAxisRight.setTypeface(typeface);
        //yAxisRight.setAxisMinimum(0);
        //yAxisRight.setAxisMaximum(average *2);

        lineChartWeight.setData(lineData);
        lineChartWeight.invalidate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            buttonGraphShowAll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowMore.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneMonth.setTextColor(getResources().getColor(android.R.color.black));
            buttonGraphShowOneWeek.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonGraphShowOneYear.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            drawChart(Period.OneMonth);
        }
    }

    private void calculateStartAndEndTime(Period period) {

        DateTime dateTime = new DateTime();

        switch (period) {
            case OneMonth:

                this.end = dateTime;
                this.start = dateTime.minusMonths(1);

                break;

            case OneWeek:

                this.end = dateTime;
                this.start = dateTime.minusWeeks(1);

                break;
            case OneYear:

                this.end = dateTime;
                this.start = dateTime.minusYears(1);

                break;
        }
    }

    private enum Period {
        All,
        OneWeek,
        OneMonth,
        OneYear,
        Custom
    }
}
