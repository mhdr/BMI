package ir.mhdr.bmi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
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
import java.util.Locale;

import ir.mhdr.bmi.blDao.HistoryBL;
import ir.mhdr.bmi.blDao.PrivateSettingBL;
import ir.mhdr.bmi.blDao.UserBL;
import ir.mhdr.bmi.dao.History;
import ir.mhdr.bmi.dao.User;
import ir.mhdr.bmi.lib.BMI;
import ir.mhdr.bmi.lib.FirebaseUtils;
import ir.mhdr.bmi.lib.ProfileChangedListener;
import ir.mhdr.bmi.lib.Statics;


public class BmiFragment extends Fragment implements ProfileChangedListener {

    AppCompatTextView textViewProfileInfoAge;
    AppCompatTextView textViewProfileInfoName;
    AppCompatTextView textViewProfileInfoHeight;
    LinearLayout linearLayoutRangeContainer;
    AppCompatTextView textViewCurrentWeight;
    AppCompatImageButton imageButtonAddNewWeight;
    AppCompatImageButton imageButtonSwapRanges;
    ScArcGauge gauge;
    AppCompatTextView textViewBMI;
    Bitmap indicator;
    View.OnClickListener imageButtonAddNewWeight_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openWeightDialog();
        }
    };
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        if (FirebaseUtils.checkPlayServices(getContext())) {
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
            mFirebaseAnalytics.setCurrentScreen(this.getActivity(), "BmiFragment", this.getClass().getSimpleName());
            mFirebaseAnalytics.setUserProperty(FirebaseUtils.UserProperty.InstallSource, Statics.InstallSource);
        }

        textViewProfileInfoAge = (AppCompatTextView) view.findViewById(R.id.textViewProfileInfoAge);
        textViewProfileInfoName = (AppCompatTextView) view.findViewById(R.id.textViewProfileInfoName);
        textViewProfileInfoHeight = (AppCompatTextView) view.findViewById(R.id.textViewProfileInfoHeight);

        gauge = (ScArcGauge) view.findViewById(R.id.ScArcGaugeBmiGauge);
        textViewBMI = (AppCompatTextView) view.findViewById(R.id.textViewBMI);
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

        linearLayoutRangeContainer = (LinearLayout) view.findViewById(R.id.linearLayoutRangeContainer);
        textViewCurrentWeight = (AppCompatTextView) view.findViewById(R.id.textViewCurrentWeight);

        imageButtonAddNewWeight = (AppCompatImageButton) view.findViewById(R.id.imageButtonAddNewWeight);
        imageButtonAddNewWeight.setOnClickListener(imageButtonAddNewWeight_OnClickListener);

        imageButtonSwapRanges = (AppCompatImageButton) view.findViewById(R.id.imageButtonSwapRanges);
        imageButtonSwapRanges.setOnClickListener(imageButtonSwapRanges_OnClickListener);

        calculateAndShow(false);

        return view;
    }

    View.OnClickListener imageButtonSwapRanges_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calculateAndShow(true);
        }
    };

    public void calculateAndShow(boolean swapRanges) {
        float value = 24;
        UserBL userBL = new UserBL();
        User user = userBL.getActiveUser();

        BMI bmi = new BMI(user.getLatestHeight(), user.getLatestWeight());
        value = Float.parseFloat(String.format(Locale.US, "%.2f", bmi.calculate()));

        String name = user.getName();
        String age = String.valueOf(calculateAge(user.getBirthdate()));
        String height = String.valueOf(user.getLatestHeight());

        textViewProfileInfoAge.setText(String.format(Locale.US, "%s سال", age));
        textViewProfileInfoName.setText(String.format(Locale.US, "%s", name));
        textViewProfileInfoHeight.setText(String.format(Locale.US, "%s سانتی متر", height));

        gauge.setHighValue(value, 12, 44);

        textViewBMI.setText(String.valueOf(value));

        textViewCurrentWeight.setText(String.format(Locale.US, "%s کیلوگرم", user.getLatestWeight()));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PrivateSettingBL privateSettingBL = new PrivateSettingBL();
        int bmiRangeMode = privateSettingBL.getBmiRangeMode();

        if (!swapRanges) {

            if (bmiRangeMode == -1) {

                BmiRangeFragment bmiRangeFragment = new BmiRangeFragment();
                fragmentTransaction.replace(R.id.linearLayoutRangeContainer, bmiRangeFragment);

            } else if (bmiRangeMode == 1) {

                BmiRangeFragment bmiRangeFragment = new BmiRangeFragment();
                fragmentTransaction.replace(R.id.linearLayoutRangeContainer, bmiRangeFragment);

            } else if (bmiRangeMode == 2) {
                WeightRangeFragment weightRangeFragment = new WeightRangeFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("bmi", bmi);
                weightRangeFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.linearLayoutRangeContainer, weightRangeFragment);

            }

            fragmentTransaction.commit();

        } else {

            Fragment previousFragment = fragmentManager.findFragmentById(R.id.linearLayoutRangeContainer);

            if (previousFragment instanceof WeightRangeFragment) {

                BmiRangeFragment bmiRangeFragment = new BmiRangeFragment();

                fragmentTransaction.setCustomAnimations(R.anim.scale_up, R.anim.scale_down)
                        .replace(R.id.linearLayoutRangeContainer, bmiRangeFragment);

                privateSettingBL.setBmiRange(1);

            } else if (previousFragment instanceof BmiRangeFragment) {
                WeightRangeFragment weightRangeFragment = new WeightRangeFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("bmi", bmi);
                weightRangeFragment.setArguments(bundle);


                fragmentTransaction.setCustomAnimations(R.anim.scale_up, R.anim.scale_down)
                        .replace(R.id.linearLayoutRangeContainer, weightRangeFragment);

                privateSettingBL.setBmiRange(2);
            }

            fragmentTransaction.commit();
        }
    }

    private void openWeightDialog() {

        final UserBL userBL = new UserBL();
        final HistoryBL historyBL = new HistoryBL();
        final User user = userBL.getActiveUser();

        String valueStr = user.getLatestWeight();

        WeightFragment weightFragment = new WeightFragment();
        weightFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);

        if (valueStr.length() > 0) {
            weightFragment.setWeightValue(Double.parseDouble(valueStr));
        }

        weightFragment.setOnSaveListener(new WeightFragment.OnSaveListener() {
            @Override
            public void onSave(double value) {
                user.setLatestWeight(String.valueOf(value));
                userBL.update(user);

                DateTime current = new DateTime();

                History history = new History();
                history.setUserUuid(user.getUuid());
                history.setValue(String.valueOf(value));
                history.setDatetime(current.toString());

                long historyId = historyBL.insert(history);

                if (historyId > 0) {
                    Toast.makeText(getContext(), R.string.new_weight_saved, Toast.LENGTH_LONG).show();
                    calculateAndShow(false);
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
            FirebaseCrash.report(e);
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

        try {
            super.onResume();

            if (!getUserVisibleHint()) {
                return;
            }

            calculateAndShow(false);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onProfileChanged() {
        onResume();
    }
}
