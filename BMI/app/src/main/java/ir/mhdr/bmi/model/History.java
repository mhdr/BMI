package ir.mhdr.bmi.model;


import net.time4j.PlainDate;
import net.time4j.calendar.PersianCalendar;

import org.joda.time.DateTime;


public class History {
    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private long _id;

    private long userId;

    private String datetime;

    private String value;

    public DateTime getDatetime2()
    {
        DateTime dateTime=DateTime.parse(this.getDatetime());

        return dateTime;
    }

    public PersianCalendar getDatetime3()
    {
        DateTime dateTime=this.getDatetime2();
        PlainDate plainDate = PlainDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        net.time4j.calendar.PersianCalendar persianCalendar = plainDate.transform(net.time4j.calendar.PersianCalendar.class);
        return persianCalendar;
    }
}
