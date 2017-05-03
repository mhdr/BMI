package ir.mhdr.bmi.model;


import ir.mhdr.bmi.lib.Gender;

public class User {

    private int _id;

    private String name;

    private String birthdate;

    private int gender;

    private String latest_height;

    private String latest_weight;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public String getLatest_height() {
        return latest_height;
    }

    public void setLatest_height(String latest_height) {
        this.latest_height = latest_height;
    }

    public String getLatest_weight() {
        return latest_weight;
    }

    public void setLatest_weight(String latest_weight) {
        this.latest_weight = latest_weight;
    }
}
