package ir.mhdr.bmi.lib;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ir.mhdr.bmi.R;

public class Resources {

    private Context context;

    public Resources(Context context) {
        this.context=context;
    }

    public List<String> getGenderList()
    {
        String[] genderRes= context.getResources().getStringArray(R.array.gender_array);
        List<String> result=new ArrayList<>();

        if (genderRes.length>0)
        {
            for (String g:genderRes)
            {
                result.add(g);
            }
        }

        return result;
    }
}
