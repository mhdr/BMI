package ir.mhdr.bmi;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ir.mhdr.bmi.bl.HistoryBL;
import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.lib.DateAxisValueFormatter;
import ir.mhdr.bmi.lib.TimeDiff;
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;

public class GraphFragment extends Fragment {

    LineChart lineChartWeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        lineChartWeight = (LineChart) view.findViewById(R.id.lineChartWeight);

        return view;
    }

    private void drawChart() {
        List<Entry> entries = new ArrayList<Entry>();

        UserBL userBL = new UserBL(getContext());
        HistoryBL historyBL = new HistoryBL(getContext());

        User user = userBL.getActiveUser();
        List<History> historyList = historyBL.getHistory(user);

        for (History h : historyList) {

            TimeDiff timeDiff = new TimeDiff(h.getDatetime2());
            float value = timeDiff.getMinutes();

            Entry entry = new Entry(value, Float.parseFloat(h.getValue()));
            entries.add(entry);
        }

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/BYekan.ttf");

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setCircleColor(getResources().getColor(R.color.colorGreenDark));
        dataSet.setLineWidth(4);
        dataSet.setHighlightEnabled(false);
        dataSet.setValueTypeface(typeface);
        dataSet.setValueTextSize(10);
        LineData lineData = new LineData(dataSet);


        lineChartWeight.setBackgroundColor(getResources().getColor(R.color.colorBackground));
        lineChartWeight.animateX(300, Easing.EasingOption.EaseInExpo);

        XAxis xAxis = lineChartWeight.getXAxis();
        xAxis.setValueFormatter(new DateAxisValueFormatter(lineChartWeight));
        xAxis.setGranularity(1f); // restrict interval to 1 (minimum)
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(3);
        xAxis.setTypeface(typeface);

        lineChartWeight.setData(lineData);
        lineChartWeight.invalidate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            drawChart();
        }
    }
}
