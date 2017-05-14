package ir.mhdr.bmi.model;


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
}
