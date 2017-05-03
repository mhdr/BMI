package ir.mhdr.bmi.lib;


public enum Gender {
    Male(1),
    Female(2);

    private final int gender;

    private Gender(int i) {
        this.gender=i;
    }

    public int getGender() {
        return gender;
    }
}
