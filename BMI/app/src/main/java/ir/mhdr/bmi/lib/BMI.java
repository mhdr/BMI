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
}
