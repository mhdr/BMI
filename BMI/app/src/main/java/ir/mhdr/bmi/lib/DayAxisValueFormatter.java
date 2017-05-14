package ir.mhdr.bmi.lib;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import net.time4j.PlainDate;
import net.time4j.calendar.PersianCalendar;

import org.joda.time.DateTime;

public class DayAxisValueFormatter implements IAxisValueFormatter {

    // https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/custom/DayAxisValueFormatter.java

    private BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        long millis= (long) value;
        DateTime dateTime=new DateTime(millis);

        PlainDate plainDate=PlainDate.of(dateTime.getYear(),dateTime.getMonthOfYear(),dateTime.getDayOfMonth());
        PersianCalendar persianCalendar= plainDate.transform(PersianCalendar.class);


        return null;
    }
}
