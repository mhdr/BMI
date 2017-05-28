package ir.mhdr.bmi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sccomponents.widgets.ScArcGauge;
import com.sccomponents.widgets.ScCopier;
import com.sccomponents.widgets.ScDrawer;
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

import ir.mhdr.bmi.bl.HistoryBL;
import ir.mhdr.bmi.bl.UserBL;
import ir.mhdr.bmi.lib.BMI;
import ir.mhdr.bmi.model.History;
import ir.mhdr.bmi.model.User;


public class BmiFragment extends Fragment {

    TextView textViewProfileInfoAge;
    TextView textViewProfileInfoName;
    TextView textViewProfileInfoHeight;
    TextView textViewWeightRange1;
    TextView textViewWeightRange2;
    TextView textViewWeightRange3;
    TextView textViewWeightRange4;
    TextView textViewWeightRange5;
    TextView textViewWeightRange6;
    TextView textViewWeightRange7;
    TextView textViewWeightRange8;
    TextView textViewCurrentWeight;
    ImageButton imageButtonAddNewWeight;
    ScArcGauge gauge;
    TextView textViewBMI;
    Bitmap indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        textViewProfileInfoAge = (TextView) view.findViewById(R.id.textViewProfileInfoAge);
        textViewProfileInfoName = (TextView) view.findViewById(R.id.textViewProfileInfoName);
        textViewProfileInfoHeight = (TextView) view.findViewById(R.id.textViewProfileInfoHeight);

        gauge = (ScArcGauge) view.findViewById(R.id.ScArcGaugeBmiGauge);
        textViewBMI = (TextView) view.findViewById(R.id.textViewBMI);
        indicator = BitmapFactory.decodeResource(this.getResources(), R.drawable.indicator);

        gauge.setAngleStart(-235);
        gauge.setAngleSweep(290);
        gauge.setStrokeSize(60);

        int screenWidth = android.content.res.Resources.getSystem().getDisplayMetrics().widthPixels;
        //int screenHeight= android.content.res.Resources.getSystem().getDisplayMetrics().heightPixels;

        int paddingLeftRight = (int) (screenWidth / 3);
        gauge.setPadding(paddingLeftRight, 30, paddingLeftRight, 5);

        gauge.setStrokeColors(generateStrokeColors());
        gauge.setStrokeColorsMode(ScFeature.ColorsMode.SOLID);

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

        textViewWeightRange1 = (TextView) view.findViewById(R.id.textViewWeightRange1);
        textViewWeightRange2 = (TextView) view.findViewById(R.id.textViewWeightRange2);
        textViewWeightRange3 = (TextView) view.findViewById(R.id.textViewWeightRange3);
        textViewWeightRange4 = (TextView) view.findViewById(R.id.textViewWeightRange4);
        textViewWeightRange5 = (TextView) view.findViewById(R.id.textViewWeightRange5);
        textViewWeightRange6 = (TextView) view.findViewById(R.id.textViewWeightRange6);
        textViewWeightRange7 = (TextView) view.findViewById(R.id.textViewWeightRange7);
        textViewWeightRange8 = (TextView) view.findViewById(R.id.textViewWeightRange8);

        textViewCurrentWeight = (TextView) view.findViewById(R.id.textViewCurrentWeight);

        imageButtonAddNewWeight = (ImageButton) view.findViewById(R.id.imageButtonAddNewWeight);
        imageButtonAddNewWeight.setOnClickListener(imageButtonAddNewWeight_OnClickListener);

        calculateAndShow();

        return view;
    }

    public void calculateAndShow() {
        float value = 24;
        UserBL userBL = new UserBL(getContext());
        User user = userBL.getActiveUser();

        BMI bmi = new BMI(user.getLatestHeight(), user.getLatestWeight());
        value = Float.parseFloat(String.format("%.2f", bmi.calculate()));

        String name = user.getName();
        String age = String.valueOf(calculateAge(user.getBirthdate()));
        String height = String.valueOf(user.getLatestHeight());

        textViewProfileInfoAge.setText(String.format("%s سال", age));
        textViewProfileInfoName.setText(String.format("%s", name));
        textViewProfileInfoHeight.setText(String.format("%s سانتی متر", height));

        gauge.setHighValue(value, 12, 44);

        textViewBMI.setText(String.valueOf(value));

        textViewWeightRange1.setText(String.format("کمتر از %.2f کیلوگرم", bmi.weightPoint1()));
        textViewWeightRange2.setText(String.format("%.2f - %.2f کیلوگرم", bmi.weightPoint1(), bmi.weightPoint2()));
        textViewWeightRange3.setText(String.format("%.2f - %.2f کیلوگرم", bmi.weightPoint2(), bmi.weightPoint3()));
        textViewWeightRange4.setText(String.format("%.2f - %.2f کیلوگرم", bmi.weightPoint3(), bmi.weightPoint4()));
        textViewWeightRange5.setText(String.format("%.2f - %.2f کیلوگرم", bmi.weightPoint4(), bmi.weightPoint5()));
        textViewWeightRange6.setText(String.format("%.2f - %.2f کیلوگرم", bmi.weightPoint5(), bmi.weightPoint6()));
        textViewWeightRange7.setText(String.format("%.2f - %.2f کیلوگرم", bmi.weightPoint6(), bmi.weightPoint7()));
        textViewWeightRange8.setText(String.format("بیشتر از %.2f کیلوگرم", bmi.weightPoint7()));

        textViewCurrentWeight.setText(String.format("%s کیلوگرم", user.getLatestWeight()));
    }

    View.OnClickListener imageButtonAddNewWeight_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openWeightDialog();
        }
    };

    private void openWeightDialog() {

        final UserBL userBL = new UserBL(getContext());
        final HistoryBL historyBL = new HistoryBL(getContext());
        final User user = userBL.getActiveUser();

        String valueStr = user.getLatestWeight();

        WeightFragment weightFragment = new WeightFragment();

        if (valueStr.length() > 0) {
            weightFragment.setWeightValue(Double.parseDouble(valueStr));
        }

        weightFragment.setOnSaveListener(new WeightFragment.OnSaveListener() {
            @Override
            public void onSave(double value) {
                user.setLatestWeight(String.valueOf(value));
                int rows_affected = userBL.update(user);

                DateTime current = new DateTime();

                History history = new History();
                history.setUserId(user.getId());
                history.setValue(String.valueOf(value));
                history.setDatetime(current.toString());

                long historyId = historyBL.insert(history);

                if (historyId > 0) {
                    Toast.makeText(getContext(), R.string.new_weight_saved, Toast.LENGTH_LONG).show();
                    calculateAndShow();
                }
            }
        });
        weightFragment.show(getFragmentManager(), "weight");
    }

    private int[] generateStrokeColors() {

        int length = 58;
        int range1 = 4;
        int range2 = 2;
        int range3 = 5;
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
        int result = 0;

        String pattern = "yyyy-MM-dd";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date birth = null;

        try {
            birth = dateFormat.parse(birthdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateTime dateTime1 = new DateTime(birth);
        DateTime dateTime2 = new DateTime();

        Period period = new Period(dateTime1, dateTime2);
        result = period.getYears();

        return result;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }

        calculateAndShow();
    }
}
