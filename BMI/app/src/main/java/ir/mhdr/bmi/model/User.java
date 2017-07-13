package ir.mhdr.bmi.model;


import ir.mhdr.bmi.lib.Gender;

public class User {
    private long id;
    private String name;
    private String birthdate;
    private int gender;
    private String latestHeight;
    private String latestWeight;
    private int isActive;

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLatestHeight() {
        return latestHeight;
    }

    public void setLatestHeight(String latestHeight) {
        this.latestHeight = latestHeight;
    }

    public String getLatestWeight() {
        return latestWeight;
    }

    public void setLatestWeight(String latestWeight) {
        this.latestWeight = latestWeight;
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

    public boolean getIsActiveX() {
        if (this.isActive == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setIsActiveX(boolean value) {
        if (value) {
            this.isActive = 1;
        } else {
            this.isActive = 0;
        }
    }
}
