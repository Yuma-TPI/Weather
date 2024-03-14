package com.example.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class AsyncGetJson extends AsyncTask<String, Integer, JSONObject> {
    private WeakReference<TextView> textArea;
    private WeakReference<TextView> textWeather;

    public AsyncGetJson(Context context){
        super();
        MainActivity activity = (MainActivity) context;
        textArea = new WeakReference<>((TextView) activity.findViewById(R.id.textArea));
        textWeather = new WeakReference<>((TextView) activity.findViewById(R.id.textWeather));
    }

    @Override
    protected JSONObject doInBackground(String... parms){
        HttpsURLConnection con = null;
        JSONObject json = new JSONObject();
        StringBuilder builder = new StringBuilder();
        try{
            URL url = new URL(parms[0]);
            con = (HttpsURLConnection) url.openConnection();
            InputStream stream = con.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String line;
            while((line = reader.readLine()) != null)
                builder.append(line);
            stream.close();

            json = new JSONObject(builder.toString());

        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }finally {
            con.disconnect();
        }

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject json){
        try{
            textArea.get().setText(json.getString("targetArea"));
            textWeather.get().setText(json.getString("text"));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
