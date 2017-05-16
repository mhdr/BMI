package ir.mhdr.bmi.lib;


import org.joda.time.DateTime;
import org.joda.time.Period;

public class TimeDiff {

    private DateTime ref=new DateTime(2017,1,1,0,0,0);
    private DateTime dateTime;

    public TimeDiff(DateTime dateTime)
    {
        this.dateTime=dateTime;
    }

    private long getSecondsInMinute()
    {
        long result=60;
        return result;
    }

    public long getSeconds()
    {
        long result=0;
        Period period=new Period(ref,dateTime);

        return result;
    }
}
