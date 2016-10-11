package com.example.user.ling.tranlate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.user.ling.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import static com.example.user.ling.tranlate.YandexTranslatorAPI.ENCODING;
import static com.example.user.ling.tranlate.YandexTranslatorAPI.activity;

public abstract class YandexTranslatorAPI {
    //Encoding type
    protected static final String ENCODING = "UTF-8";

    protected static String apiKey;

    protected static final String PARAM_API_KEY = "key=",
            PARAM_LANG_PAIR = "&lang=",
            PARAM_TEXT = "&text=";
    public static Activity activity;

    public static void setKey(final String pKey) {
        apiKey = pKey;
    }

    private static String retrieveResponse(final URL url) throws Exception {
        RetrieveFeedTask dd = new RetrieveFeedTask();
        AsyncTask<URL, Void, String> sd= dd.execute(url);
        return sd.get();
    }

    protected static String retrievePropArrString(final URL url, final String jsonValProperty, Activity activity) throws Exception {
        YandexTranslatorAPI.activity = activity;
        String response = retrieveResponse(url);
        return response;

    }

    protected static String validateServiceState() throws Exception {
        if(apiKey==null||apiKey.length()<27) {

          return  "INVALID_API_KEY - Please set the API Key with your Yandex API Key";
        }
        return null;
    }

}
class RetrieveFeedTask extends AsyncTask<URL, Void, String> {
    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        dialog = Utils.factoryDialog(activity, "Запрос на Яндекс", null);
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    protected String doInBackground(URL... url) {
        HttpsURLConnection uc=null;
        try {
            uc = (HttpsURLConnection) url[0].openConnection();
            uc.setRequestProperty("Content-Type","text/plain; charset=" + ENCODING);
            uc.setRequestProperty("Accept-Charset",ENCODING);
            uc.setRequestMethod("GET");


            final int responseCode = uc.getResponseCode();
            final String result = inputStreamToString(uc.getInputStream());
            if(responseCode!=200) {
                throw new Exception("Error from Yandex API: " + result);
            }
            return result;
        }catch (Exception ex){
            return "Возможно проблемы со связью:"+ex.getMessage();
        }
        finally {
            if(uc!=null) {
                uc.disconnect();
            }
        }

    }
    private static String inputStreamToString(final InputStream inputStream) throws Exception {
        final StringBuilder outputBuilder = new StringBuilder();

        try {
            String string;
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
                while (null != (string = reader.readLine())) {
                    // TODO Can we remove this?
                    // Need to strip the Unicode Zero-width Non-breaking Space. For some reason, the Microsoft AJAX
                    // services prepend this to every response
                    outputBuilder.append(string.replaceAll("\uFEFF", ""));
                }
            }
        } catch (Exception ex) {
            throw new Exception("[yandex-translator-api] Error reading translation stream.", ex);
        }
        return outputBuilder.toString();
    }
}
