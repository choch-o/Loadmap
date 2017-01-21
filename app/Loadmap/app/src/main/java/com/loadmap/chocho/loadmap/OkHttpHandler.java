package com.loadmap.chocho.loadmap;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by q on 2017-01-22.
 */

public class OkHttpHandler extends AsyncTask<String, Void, String> {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    protected String doInBackground(String... params) {
        Request request;
        // POST
        if (params.length > 1) {
            try {
                // JSONArray contactArray = new JSONArray(params[1]);
                // Log.d("JSON ARRAY NEW", contactArray.toString());

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("JSON ERROR", "TT");
            }

            RequestBody requestBody = RequestBody.create(JSON, params[1]);
            Log.d("REQUEST BODY", params[0]);
            request = new Request.Builder()
                    .url(params[0])
                    .post(requestBody)
                    .build();

        } else {
            request = new Request.Builder()
                    .url(params[0])
                    .get()
                    .build();
        }
        // GET
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "FAILED";
    }

    protected void onPostExecute(String result) {
        Log.d("POST response", result);
    }
}
