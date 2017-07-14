package ir.mhdr.bmi.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.joda.time.DateTime;

import ir.pupli.jalalicalendarlib.JCalendar;

@Entity
public class History {
    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String uuid;

    @Index()
    private String userUuid;
    private String datetime;
    private String value;
    private String timestamp;
    private boolean isRemoved;


    @Generated(hash = 680039804)
    public History(Long id, String uuid, String userUuid, String datetime, String value, String timestamp,
            boolean isRemoved) {
        this.id = id;
        this.uuid = uuid;
        this.userUuid = userUuid;
        this.datetime = datetime;
        this.value = value;
        this.timestamp = timestamp;
        this.isRemoved = isRemoved;
    }

    @Generated(hash = 869423138)
    public History() {
    }


    public DateTime getDatetime2() {
        DateTime dateTime = DateTime.parse(this.getDatetime());

        return dateTime;
    }

    public JCalendar getDatetime3() {
        DateTime dateTime = this.getDatetime2();

        JCalendar jCalendar = new JCalendar(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), true);

        return jCalendar;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserUuid() {
        return this.userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getIsRemoved() {
        return this.isRemoved;
    }

    public void setIsRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }


}
