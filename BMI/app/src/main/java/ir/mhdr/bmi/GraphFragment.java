package ir.mhdr.bmi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
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
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;

public class GraphFragment extends Fragment {

    LineChart lineChartWeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        lineChartWeight = (LineChart) view.findViewById(R.id.lineChartWeight);
        XAxis xAxis= lineChartWeight.getXAxis();
        xAxis.setValueFormatter(new DateAxisValueFormatter(lineChartWeight));

        return view;
    }

    private void drawChart() {
        List<Entry> entries = new ArrayList<Entry>();

        UserBL userBL = new UserBL(getContext());
        HistoryBL historyBL = new HistoryBL(getContext());

        User user = userBL.getActiveUser();
        List<History> historyList = historyBL.getHistory(user);

        DateTime refDate=new DateTime(2017,1,1,0,0,0);

        for (History h : historyList) {
            Period period=new Period(refDate,h.getDatetime2());
            float seconds= Timestamp.valueOf(h.getDatetime()).getTime();
            // todo continue
            Entry entry = new Entry(seconds, Float.parseFloat(h.getValue()));
            entries.add(entry);
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setCircleColor(getResources().getColor(R.color.colorGreenDark));
        dataSet.setLineWidth(4);
        LineData lineData = new LineData(dataSet);
        lineChartWeight.setData(lineData);
        lineChartWeight.setBackgroundColor(getResources().getColor(R.color.colorBackground));
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
