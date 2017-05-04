package ir.mhdr.bmi.model;


import ir.mhdr.bmi.lib.HistoryType;

public class History {
    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private long _id;

    private long userId;

    private String datetime;

    private String value;

    private int type;

    public HistoryType getTypeEnum() {
        switch (this.type) {
            case 1:
                return HistoryType.Weight;
            case 2:
                return HistoryType.Height;
            default:
                return HistoryType.Weight;
        }
    }

    public void setHistoryEnum(HistoryType history) {
        switch (history) {
            case Weight:
                this.type = 1;
                break;
            case Height:
                this.type = 2;
                break;
        }
    }
}
