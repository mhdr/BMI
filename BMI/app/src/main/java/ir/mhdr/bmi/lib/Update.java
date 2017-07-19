package ir.mhdr.bmi.lib;

import com.google.firebase.crash.FirebaseCrash;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ir.mhdr.bmi.BuildConfig;

public class Update {

    private UpdateListener mUpdateListener;

    public void setUpdateListener(UpdateListener listener) {
        mUpdateListener = listener;
    }

    public void Check() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String baseUrl = "https://update.pupli.ir/bmi/";

                    String urlStr = baseUrl + "version.xml";

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();

                    URL url = new URL(urlStr);
                    InputStream inputStream = url.openStream();

                    Document doc = db.parse(inputStream);
                    doc.getDocumentElement().normalize();

                    Element element1 = doc.getElementById("versionCode");
                    int versionCode = Integer.parseInt(element1.getTextContent());

                    Element element2 = doc.getElementById("versionName");
                    String versionName = element2.getTextContent();

                    Element element3 = doc.getElementById("file");
                    String apk = element3.getTextContent();

                    inputStream.close();

                    if (versionCode> BuildConfig.VERSION_CODE)
                    {
                        mUpdateListener.newUpdateAvailable(new UpdateInfo(baseUrl,versionCode,versionName,apk));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                }
            }
        });

        thread.start();
    }

    public interface UpdateListener {

        void newUpdateAvailable(UpdateInfo updateInfo);

    }

    public class UpdateInfo {
        public String baseUrl;
        public int versionCode;
        public String versionName;
        public String file;

        public UpdateInfo() {

        }

        public UpdateInfo(String baseUrl, int versionCode, String versionName, String file) {
            this.baseUrl = baseUrl;
            this.versionCode = versionCode;
            this.versionName = versionName;
            this.file = file;
        }
    }

}
