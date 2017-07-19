package ir.mhdr.bmi;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import ir.mhdr.bmi.lib.FirebaseUtils;
import ir.mhdr.bmi.lib.Update;


public class UpdateFragment extends DialogFragment {


    ProgressBar progressBarUpdate;
    AppCompatButton buttonStopUpdate;
    boolean cancelCalled = false;
    Update.UpdateInfo updateInfo;
    AppCompatTextView textViewProgressPercent;
    AppCompatTextView textViewProgressBytes;

    private FirebaseAnalytics mFirebaseAnalytics;

    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update, container, false);

        if (FirebaseUtils.checkPlayServices(getContext())) {

            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        }

        // set this dialog modal
        this.setCancelable(false);
        getDialog().setTitle(getResources().getString(R.string.updating));

        progressBarUpdate = (ProgressBar) view.findViewById(R.id.progressBarUpdate);
        buttonStopUpdate = (AppCompatButton) view.findViewById(R.id.buttonStopUpdate);
        buttonStopUpdate.setOnClickListener(buttonStopUpdate_OnClickListener);
        textViewProgressBytes = (AppCompatTextView) view.findViewById(R.id.textViewProgressBytes);
        textViewProgressPercent = (AppCompatTextView) view.findViewById(R.id.textViewProgressPercent);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                download();
            }
        });

        thread.start();

        return view;
    }

    public void setUpdateInfo(Update.UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    View.OnClickListener buttonStopUpdate_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelCalled = true;
        }
    };

    private void download() {
        try {

            if (updateInfo == null) {
                dismiss();
                return;
            }

            String fileUrl = updateInfo.baseUrl + updateInfo.file;
            URL url = new URL(fileUrl);

            URLConnection conection = url.openConnection();
            conection.connect();

            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // in kb
            final int fLengthOfFile = lenghtOfFile / 1000;

            // initialize progressbar
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBarUpdate.setProgress(0);
                    String progress = 0 + "%";
                    textViewProgressPercent.setText(progress);
                    String bStr = String.format(Locale.US, "%d/%d", 0, fLengthOfFile);
                    textViewProgressBytes.setText(bStr);
                }
            });

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            File folder = getContext().getFilesDir();

            String path = folder.getAbsolutePath() + "/" + updateInfo.file;
            File saveFile = new File(path);

            saveFile.createNewFile();

            // Output stream to write file
            OutputStream output = new FileOutputStream(saveFile, false);

            byte data[] = new byte[1024];

            int total = 0;
            int count = 0;

            while ((count = input.read(data)) != -1) {

                if (cancelCalled) {
                    dismiss();
                    return;
                }

                total += count;

                final int progressPercent = (int) ((total * 100) / lenghtOfFile);
                final int fTotal = total / 1000;


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarUpdate.setProgress(progressPercent);
                        String progress = progressPercent + "%";
                        textViewProgressPercent.setText(progress);
                        String bStr = String.format(Locale.US, "%d/%d", fTotal, fLengthOfFile);
                        textViewProgressBytes.setText(bStr);
                    }
                });

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            saveFile = new File(path);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                saveFile.setReadable(true, false);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(saveFile), "application/vnd.android.package-archive");
                getContext().getApplicationContext().startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri fileUri = FileProvider.getUriForFile(getContext(),
                        "ir.mhdr.provider",
                        saveFile);

                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }

            dismiss();

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
            dismiss();
        }
    }
}
