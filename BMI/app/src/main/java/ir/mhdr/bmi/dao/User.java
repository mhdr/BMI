package ir.mhdr.bmi.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import ir.mhdr.bmi.blDao.PrivateSettingBL;
import ir.mhdr.bmi.lib.Gender;

@Entity()
public class User {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String uuid;
    private String name;
    private String birthdate;
    private int gender;
    private String latestHeight;
    private String latestWeight;
    private boolean isRemoved;

    // sync
    private String timestamp;
    private boolean syncState;
    private String datetimeModified;
    //

    public boolean isActive() {
        PrivateSettingBL privateSettingBL = new PrivateSettingBL();
        User user = privateSettingBL.getActiveUser();

        if (user.getUuid() == this.getUuid()) {
            return true;
        }

        return false;
    }

    public Gender getGenderX() {
        switch (this.gender) {
            case 1:
                return Gender.Male;
            case 2:
                return Gender.Female;
            default:
                return Gender.Male;
        }
    }

    public void setGenderX(Gender gender) {
        switch (gender) {
            case Male:
                this.gender = 1;
                break;
            case Female:
                this.gender = 2;
                break;
        }
    }

    @Generated(hash = 2123755141)
    public User(Long id, String uuid, String name, String birthdate, int gender,
            String latestHeight, String latestWeight, boolean isRemoved,
            String timestamp, boolean syncState, String datetimeModified) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.latestHeight = latestHeight;
        this.latestWeight = latestWeight;
        this.isRemoved = isRemoved;
        this.timestamp = timestamp;
        this.syncState = syncState;
        this.datetimeModified = datetimeModified;
    }

    @Generated(hash = 586692638)
    public User() {
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBirthdate() {
        return this.birthdate;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    public int getGender() {
        return this.gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public String getLatestHeight() {
        return this.latestHeight;
    }
    public void setLatestHeight(String latestHeight) {
        this.latestHeight = latestHeight;
    }
    public String getLatestWeight() {
        return this.latestWeight;
    }
    public void setLatestWeight(String latestWeight) {
        this.latestWeight = latestWeight;
    }
    public boolean getIsRemoved() {
        return this.isRemoved;
    }
    public void setIsRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }
    public boolean getSyncState() {
        return this.syncState;
    }
    public void setSyncState(boolean syncState) {
        this.syncState = syncState;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDatetimeModified() {
        return this.datetimeModified;
    }

    public void setDatetimeModified(String datetimeModified) {
        this.datetimeModified = datetimeModified;
    }
}
