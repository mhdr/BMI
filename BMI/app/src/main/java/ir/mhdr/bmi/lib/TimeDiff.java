package ir.mhdr.bmi.lib;


import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.Seconds;

public class TimeDiff {

    private DateTime ref = new DateTime(2017, 1, 1, 0, 0, 0);

    public DateTime getDateTime() {
        return dateTime;
    }

    private DateTime dateTime;

    public TimeDiff(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    static TimeDiff fromMinutes(int minutes)
    {
        DateTime ref = new DateTime(2017, 1, 1, 0, 0, 0);
        DateTime min=ref.plusMinutes(minutes);

        TimeDiff timeDiff=new TimeDiff(min);
        return timeDiff;
    }

    public long getSeconds() {
        Seconds diff = Seconds.secondsBetween(ref, dateTime);
        long result = diff.getSeconds();

        return result;
    }

    public long getMinutes() {
        Minutes diff = Minutes.minutesBetween(ref, dateTime);
        long result = diff.getMinutes();


        return result;
    }
}
