package ir.mhdr.bmi.lib;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.List;
import java.util.Locale;

import ir.pupli.jalalicalendarlib.JCalendar;

public class DateAxisValueFormatter implements IAxisValueFormatter {

    // https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/custom/DayAxisValueFormatter.java

    private BarLineChartBase<?> chart;

    public DateAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int minutes = (int) value;
        DateTime dateTime = TimeDiff.fromMinutes(minutes).getDateTime();
        JCalendar persianCalendar = TimeDiff.fromMinutes(minutes).getPersianCalendar();

        String result = "";

        float visibleRange = chart.getVisibleXRange();


        if (visibleRange < 60) {
            // less than 1 hour

            result = String.format(Locale.US, "%d:%d", dateTime.getHourOfDay(), dateTime.getMinuteOfHour());

        } else if (visibleRange < 60 * 24) {
            // less than 1 day

            result = String.format(Locale.US, "%d:%d", dateTime.getHourOfDay(), dateTime.getMinuteOfHour());

        } else if (visibleRange < 60 * 24 * 30) {
            // less than 1 month

            result = String.format(Locale.US, "%d %s", persianCalendar.getDay(), this.getMonthName(persianCalendar.getMonth()));

        } else if (visibleRange < 60 * 24 * 30 * 12) {
            // less than 1 year

            result = String.format(Locale.US, "%s", String.valueOf(persianCalendar.getMonthString()));
            chart.getXAxis().setLabelCount(2);

        } else if (visibleRange > 60 * 24 * 30 * 12) {
            // more than 1 year
            result = String.format(Locale.US, "%d", persianCalendar.getYear());

        } else {

        }


        return result;
    }

    private String getMonthName(int month) {
        String result = "";

        switch (month) {
            case 1:
                result = "فروردین";
                break;
            case 2:
                result = "اردیبهشت";
                break;
            case 3:
                result = "خرداد";
                break;
            case 4:
                result = "تیر";
                break;
            case 5:
                result = "مرداد";
                break;
            case 6:
                result = "شهریور";
                break;
            case 7:
                result = "مهر";
                break;
            case 8:
                result = "آبان";
                break;
            case 9:
                result = "آذر";
                break;
            case 10:
                result = "دی";
                break;
            case 11:
                result = "بهمن";
                break;
            case 12:
                result = "اسفند";
                break;
            default:
                result = "";
        }

        return result;
    }
}
