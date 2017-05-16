package ir.mhdr.bmi.lib;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import net.time4j.PlainDate;
import net.time4j.calendar.PersianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class DateAxisValueFormatter implements IAxisValueFormatter {

    // https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/custom/DayAxisValueFormatter.java

    private BarLineChartBase<?> chart;

    public DateAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int seconds = (int) value;
        DateTime refDate=new DateTime(2017,1,1,0,0,0);
        DateTime dateTime= refDate.plusSeconds(seconds);

        PlainDate plainDate = PlainDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        PersianCalendar persianCalendar = plainDate.transform(PersianCalendar.class);

        String result = "";

        float visibleRange = chart.getVisibleXRange();

        if (visibleRange < 1) {
            // less than 1 second

            result = String.format("%d:%d", dateTime.getHourOfDay(), dateTime.getMinuteOfHour());

        } else if (visibleRange < 60) {
            // less than 1 minute

            result = String.format("%d:%d", dateTime.getHourOfDay(), dateTime.getMinuteOfHour());

        } else if (visibleRange < 60 * 60) {
            // less than 1 hour

            result = String.format("%d:%d", dateTime.getHourOfDay(), dateTime.getMinuteOfHour());

        } else if (visibleRange < 60 * 60 * 24) {
            // less than 1 day

            result = String.format("%d:%d", dateTime.getHourOfDay(), dateTime.getMinuteOfHour());

        } else if (visibleRange < 60 * 60 * 24 * 30) {
            // less than 1 month

            result = String.format("%d %s", persianCalendar.getDayOfMonth(), persianCalendar.getMonth().toString());

        } else if (visibleRange < 60 * 60 * 24 * 30) {
            // less than 1 year
        } else if (visibleRange > 60 * 60 * 24 * 30) {
            // more than 1 year
        }

        return result;
    }
}
