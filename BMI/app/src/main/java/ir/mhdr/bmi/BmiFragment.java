package ir.mhdr.bmi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sccomponents.widgets.ScArcGauge;
import com.sccomponents.widgets.ScCopier;
import com.sccomponents.widgets.ScFeature;
import com.sccomponents.widgets.ScGauge;
import com.sccomponents.widgets.ScNotches;
import com.sccomponents.widgets.ScPointer;
import com.sccomponents.widgets.ScWriter;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.lib.BMI;
import ir.mhdr.bmi.model.User;


public class BmiFragment extends Fragment {

    TextView textViewProfileInfoAge;
    TextView textViewProfileInfoName;
    TextView getTextViewProfileInfoHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        textViewProfileInfoAge = (TextView) view.findViewById(R.id.textViewProfileInfoAge);
        textViewProfileInfoName = (TextView) view.findViewById(R.id.textViewProfileInfoName);
        getTextViewProfileInfoHeight = (TextView) view.findViewById(R.id.textViewProfileInfoHeight);

        float value = 24;
        UserBL userBL=new UserBL(getContext());
        User user=userBL.getActiveUser();

        BMI bmi=new BMI(user.getLatestHeight(),user.getLatestWeight());
        value=Float.parseFloat(String.format("%.2f",bmi.calculate()));

        String name=user.getName();
        String age= String.valueOf(calculateAge(user.getBirthdate()));
        String height=String.valueOf(user.getLatestHeight());

        textViewProfileInfoAge.setText(String.format("%s سال",age));
        textViewProfileInfoName.setText(String.format("%s",name));
        getTextViewProfileInfoHeight.setText(String.format("%s سانتی متر",height));

        final ScArcGauge gauge = (ScArcGauge) view.findViewById(R.id.ScArcGaugeBmiGauge);

        final TextView textViewBMI = (TextView) view.findViewById(R.id.textViewBMI);

        final Bitmap indicator = BitmapFactory.decodeResource(this.getResources(), R.drawable.indicator);

        gauge.setAngleStart(-235);
        gauge.setAngleSweep(290);
        gauge.setStrokeSize(60);
        gauge.setHighValue(value, 12, 44);

        int screenWidth= android.content.res.Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight= android.content.res.Resources.getSystem().getDisplayMetrics().heightPixels;

        int paddingLeftRight= (int) (screenWidth/3.5);
        gauge.setPadding(paddingLeftRight,30,paddingLeftRight,5);

        textViewBMI.setText(String.valueOf(value));

        gauge.setStrokeColors(generateStrokeColors());
        gauge.setStrokeColorsMode(ScFeature.ColorsMode.SOLID);


        gauge.setOnEventListener(new ScGauge.OnEventListener() {
            @Override
            public void onValueChange(float lowValue, float highValue) {
                //counter.setText(String.valueOf(highValue));
            }
        });

        gauge.setOnDrawListener(new ScGauge.OnDrawListener() {
            @Override
            public void onBeforeDrawCopy(ScCopier.CopyInfo info) {
            }

            @Override
            public void onBeforeDrawNotch(ScNotches.NotchInfo info) {
            }

            @Override
            public void onBeforeDrawPointer(ScPointer.PointerInfo info) {
                if (info.source.getTag() == ScGauge.HIGH_POINTER_IDENTIFIER) {
                    info.offset.x = -indicator.getWidth() / 2;
                    info.offset.y = -indicator.getHeight() / 2;
                    info.bitmap = indicator;
                }
            }

            @Override
            public void onBeforeDrawToken(ScWriter.TokenInfo info) {
            }
        });

        return view;
    }

    private int[] generateStrokeColors() {

        int length = 56;
        int range1 = 4;
        int range2 = 2;
        int range3 = 3;
        int range4 = 13;
        int range5 = 10;
        int range6 = 10;
        int range7 = 10;
        int range8 = 4;

        int color1 = getResources().getColor(R.color.color_bmi_range_1);
        int color2 = getResources().getColor(R.color.color_bmi_range_2);
        int color3 = getResources().getColor(R.color.color_bmi_range_3);
        int color4 = getResources().getColor(R.color.color_bmi_range_4);
        int color5 = getResources().getColor(R.color.color_bmi_range_5);
        int color6 = getResources().getColor(R.color.color_bmi_range_6);
        int color7 = getResources().getColor(R.color.color_bmi_range_7);
        int color8 = getResources().getColor(R.color.color_bmi_range_8);

        int[] result = new int[length];

        for (int i = 0; i < length; i++) {

            if (range1 > 0) {
                result[i] = color1;
                range1--;
            } else if (range2 > 0) {
                result[i] = color2;
                range2--;
            } else if (range3 > 0) {
                result[i] = color3;
                range3--;
            } else if (range4 > 0) {
                result[i] = color4;
                range4--;
            } else if (range5 > 0) {
                result[i] = color5;
                range5--;
            } else if (range6 > 0) {
                result[i] = color6;
                range6--;
            } else if (range7 > 0) {
                result[i] = color7;
                range7--;
            } else if (range8 > 0) {
                result[i] = color8;
                range8--;
            }
        }

        return result;
    }

    private int calculateAge(String birthdate) {
        int result=0;

        String pattern = "yyyy-MM-dd";

        SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
        Date birth= null;

        try {
            birth = dateFormat.parse(birthdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateTime dateTime1=new DateTime(birth);
        DateTime dateTime2=new DateTime();

        Period period=new Period(dateTime1,dateTime2);
        result= period.getYears();

        return result;
    }
}
