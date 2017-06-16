package ir.mhdr.bmi;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import ir.mhdr.bmi.lib.Update;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends DialogFragment {


    ProgressBar progressBarUpdate;
    AppCompatButton buttonStopUpdate;
    boolean cancelCalled = false;
    Update.UpdateInfo updateInfo;

    private FirebaseAnalytics mFirebaseAnalytics;

    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update, container, false);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        // set this dialog modal
        setCancelable(false);

        progressBarUpdate = (ProgressBar) view.findViewById(R.id.progressBarUpdate);
        buttonStopUpdate = (AppCompatButton) view.findViewById(R.id.buttonStopUpdate);
        buttonStopUpdate.setOnClickListener(buttonStopUpdate_OnClickListener);

        getDialog().setTitle(getResources().getString(R.string.updating));


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
                return;
            }

            String fileUrl = updateInfo.baseUrl + updateInfo.file;
            String savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + updateInfo.file;
            URL url = new URL(fileUrl);

            URLConnection conection = url.openConnection();
            conection.connect();

            // getting file length
            int lenghtOfFile = conection.getContentLength();


            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            OutputStream output = new FileOutputStream(savePath, false);

            byte data[] = new byte[1024];

            long total = 0;
            int count = 0;

            while ((count = input.read(data)) != -1) {

                if (cancelCalled) {
                    dismiss();
                    return;
                }

                total += count;

                final int progress = (int) ((total * 100) / lenghtOfFile);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarUpdate.setProgress(progress);
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

            dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
