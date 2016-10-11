package com.example.user.ling.tranlate;


import android.app.Activity;

import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by USER on 06.10.2016.
 */

public final class Translate extends YandexTranslatorAPI {

    private static final String SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    private static final String TRANSLATION_LABEL = "text";
    private Translate(){};

    public static String execute(final String text, final Language from, final Language to, Activity activity) throws Exception {

        final String params =
                PARAM_API_KEY + URLEncoder.encode(apiKey,ENCODING)
                        + PARAM_LANG_PAIR + URLEncoder.encode(from.toString(),ENCODING) + URLEncoder.encode("-",ENCODING) + URLEncoder.encode(to.toString(),ENCODING)
                        + PARAM_TEXT + URLEncoder.encode(text,ENCODING);
        final URL url = new URL(SERVICE_URL + params);
        return retrievePropArrString(url, TRANSLATION_LABEL,activity).trim();
    }
}

