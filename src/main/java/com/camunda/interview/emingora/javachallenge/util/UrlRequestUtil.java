package com.camunda.interview.emingora.javachallenge.util;

import org.apache.commons.io.IOUtils;
import org.camunda.bpm.engine.impl.util.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class UrlRequestUtil {

    public static JSONObject fetchUrl(final String urlString) {
        try {
            final URL url = new URL(urlString);
            final String json = IOUtils.toString(url, Charset.forName("UTF-8"));
            return new JSONObject(json);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error fetching JSON from %s", urlString), e);
        }
    }
}
