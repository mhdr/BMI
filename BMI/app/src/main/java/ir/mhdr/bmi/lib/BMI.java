package ir.mhdr.bmi.lib;

import java.io.Serializable;

public class BMI implements Serializable{

    private String height;
    private String weight;

    private final float range1 = 16f;
    private final float range2 = 17f;
    private final float range3 = 18.5f;
    private final float range4 = 25f;
    private final float range5 = 30f;
    private final float range6 = 35f;
    private final float range7 = 40f;

    public BMI(String height, String weight) {
        this.height = height;
        this.weight = weight;
    }

    public float calculate() {
        float heightF = Float.parseFloat(height);
        float weightF = Float.parseFloat(weight);
        float heightF2 = (heightF / 100) * (heightF / 100);
        float bmi = weightF / heightF2;

        return bmi;
    }

    public float weightPoint1() {
        float heightF = Float.parseFloat(height);
        float heightF2 = (heightF / 100) * (heightF / 100);
        float bmi = range1;

        float weightF = bmi * heightF2;
        return weightF;
    }

    public float weightPoint2() {
        float heightF = Float.parseFloat(height);
        float heightF2 = (heightF / 100) * (heightF / 100);
        float bmi = range2;

        float weightF = bmi * heightF2;
        return weightF;
    }

    public float weightPoint3() {
        float heightF = Float.parseFloat(height);
        float heightF2 = (heightF / 100) * (heightF / 100);
        float bmi = range3;

        float weightF = bmi * heightF2;
        return weightF;
    }

    public float weightPoint4() {
        float heightF = Float.parseFloat(height);
        float heightF2 = (heightF / 100) * (heightF / 100);
        float bmi = range4;

        float weightF = bmi * heightF2;
        return weightF;
    }

    public float weightPoint5() {
        float heightF = Float.parseFloat(height);
        float heightF2 = (heightF / 100) * (heightF / 100);
        float bmi = range5;

        float weightF = bmi * heightF2;
        return weightF;
    }

    public float weightPoint6() {
        float heightF = Float.parseFloat(height);
        float heightF2 = (heightF / 100) * (heightF / 100);
        float bmi = range6;

        float weightF = bmi * heightF2;
        return weightF;
    }

    public float weightPoint7() {
        float heightF = Float.parseFloat(height);
        float heightF2 = (heightF / 100) * (heightF / 100);
        float bmi = range7;

        float weightF = bmi * heightF2;
        return weightF;
    }

    public int getRange() {
        int range = 0;
        float bmi = this.calculate();

        if (bmi < range1) {
            range = 1;
        } else if (bmi > range1 & bmi <= range2) {
            range = 2;
        } else if (bmi > range2 & bmi <= range3) {
            range = 3;
        } else if (bmi > range3 & bmi <= range4) {
            range = 4;
        } else if (bmi > range4 & bmi <= range5) {
            range = 5;
        } else if (bmi > range5 & bmi <= range6) {
            range = 6;
        } else if (bmi > range6 & bmi <= range7) {
            range = 7;
        } else if (bmi > range7) {
            range = 8;
        }

        return range;
    }
}
