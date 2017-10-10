package com.Lucifer2603.torrentHome.crawler;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangchen20
 */
public abstract class BaseCrawler {

    public static OkHttpClient client;


    static  {
        try {
            client = new OkHttpClient.Builder()
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public abstract void crawl();




    public String post(String url, Map<String, String> params) throws IOException, InterruptedException {
        FormBody.Builder formB = new FormBody.Builder();

        for (Map.Entry e : params.entrySet()) {
            formB.add((String) e.getKey(), (String) e.getValue());
        }

        Request request = new Request.Builder()
                .url(url)
                .post(formB.build())
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String get(String url) throws IOException, InterruptedException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
