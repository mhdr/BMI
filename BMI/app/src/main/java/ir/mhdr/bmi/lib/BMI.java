package ir.mhdr.bmi.lib;

public class BMI {

    private String height;
    private String weight;

    public BMI(String height,String weight)
    {
        this.height=height;
        this.weight=weight;
    }

    public float calculate()
    {
        float heightF=Float.parseFloat(height);
        float weightF=Float.parseFloat(weight);
        float heightF2=(heightF/100) * (heightF/100);
        float bmi=weightF/heightF2;

        return bmi;
    }

    public float weightPoint1()
    {
        float heightF=Float.parseFloat(height);
        float heightF2=(heightF/100) * (heightF/100);
        float bmi=16f;

        float weightF=bmi * heightF2;
        return weightF;
    }

    public float weightPoint2()
    {
        float heightF=Float.parseFloat(height);
        float heightF2=(heightF/100) * (heightF/100);
        float bmi=17f;

        float weightF=bmi * heightF2;
        return weightF;
    }

    public float weightPoint3()
    {
        float heightF=Float.parseFloat(height);
        float heightF2=(heightF/100) * (heightF/100);
        float bmi=18.5f;

        float weightF=bmi * heightF2;
        return weightF;
    }

    public float weightPoint4()
    {
        float heightF=Float.parseFloat(height);
        float heightF2=(heightF/100) * (heightF/100);
        float bmi=25f;

        float weightF=bmi * heightF2;
        return weightF;
    }

    public float weightPoint5()
    {
        float heightF=Float.parseFloat(height);
        float heightF2=(heightF/100) * (heightF/100);
        float bmi=30f;

        float weightF=bmi * heightF2;
        return weightF;
    }

    public float weightPoint6()
    {
        float heightF=Float.parseFloat(height);
        float heightF2=(heightF/100) * (heightF/100);
        float bmi=35f;

        float weightF=bmi * heightF2;
        return weightF;
    }

    public float weightPoint7()
    {
        float heightF=Float.parseFloat(height);
        float heightF2=(heightF/100) * (heightF/100);
        float bmi=40f;

        float weightF=bmi * heightF2;
        return weightF;
    }
}
