package ir.mhdr.bmi;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.mhdr.bmi.lib.Update;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmUpdateFragment extends DialogFragment {

    AppCompatTextView textViewUpdateAvailableText;
    AppCompatButton buttonUpdateYes;
    AppCompatButton buttonUpdateNo;

    public void setUpdateInfo(Update.UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    Update.UpdateInfo updateInfo;

    public ConfirmUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirm_update, container, false);

        getDialog().setTitle(getResources().getString(R.string.check_update));

        textViewUpdateAvailableText = (AppCompatTextView) view.findViewById(R.id.textViewUpdateAvailableText);
        buttonUpdateYes = (AppCompatButton) view.findViewById(R.id.buttonUpdateYes);
        buttonUpdateNo = (AppCompatButton) view.findViewById(R.id.buttonUpdateNo);

        String text1 = String.format("نسخه فعلی برنامه شما %s بوده و ", BuildConfig.VERSION_NAME);
        String text2 = String.format("بروزرسانی %s برای آن موجود است.", updateInfo.versionName);
        String text3 = String.format("آیا هم اکنون مایل به بروزرسانی هستید؟");

        String text = text1 + text2 + text3;
        textViewUpdateAvailableText.setText(text);


        buttonUpdateYes.setOnClickListener(buttonUpdateYes_OnClickListener);
        buttonUpdateNo.setOnClickListener(buttonUpdateNo_OnClickListener);

        return view;
    }


    View.OnClickListener buttonUpdateYes_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UpdateFragment updateFragment = new UpdateFragment();
            updateFragment.setUpdateInfo(updateInfo);
            updateFragment.show(getFragmentManager(), "update");
            dismiss();
        }
    };

    View.OnClickListener buttonUpdateNo_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

}
