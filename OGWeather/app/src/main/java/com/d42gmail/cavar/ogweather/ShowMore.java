package com.d42gmail.cavar.ogweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class ShowMore extends AppCompatActivity {
    ArrayList<MoreCityInfo> MCI = new ArrayList<MoreCityInfo>();
    ListView MCIview;
    ShowMoreAdapter adapter;
    String n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more);

        MCIview = (ListView) findViewById(R.id.listView2);

        Bundle b = getIntent().getExtras();
        n = b.getString("cityName");
        Log.i("ii", "" + n);

        adapter = new ShowMoreAdapter(getApplicationContext(), MCI);
        MCIview.setAdapter(adapter);

        GetMoreTask myTask = new GetMoreTask();
        myTask.execute("http://api.openweathermap.org/data/2.5/forecast/daily?q="+n+"&appid=2a8fc52212d1d020c4b3ac497469a6ef");
        adapter.notifyDataSetChanged();


    }


    private class GetMoreTask extends AsyncTask <String,Integer,MoreCityInfo>{
        @Override
        protected MoreCityInfo doInBackground(String... params) {
            MoreCityInfo current=new MoreCityInfo();
            try {
                URL u = new URL(params[0]);
                HttpURLConnection connection =
                        (HttpURLConnection) u.openConnection();
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream in = connection.getInputStream();
                StringBuilder sBuilder = new StringBuilder();
                BufferedReader bReader = new BufferedReader
                        (new InputStreamReader(in));
                String line;
                while((line = bReader.readLine()) != null){
                    sBuilder.append(line);
                }
                String JSONString = sBuilder.toString();
                current = readJSONfromString(JSONString);
            } catch (Exception e) {
                e.printStackTrace();}
            return current;
        }

        @Override
        protected void onPostExecute(MoreCityInfo moreCityInfo) {
            adapter.notifyDataSetChanged();
        }

        private MoreCityInfo readJSONfromString(String jsonString) {

            for(int j = 8; j>0;
                j--) {
                try {
                    MoreCityInfo MoreObj=new MoreCityInfo();
                    MoreObj.setCitaName(n);

                    JSONObject completeData = new JSONObject(jsonString);
                    JSONArray test = completeData.getJSONArray("list");
                    JSONObject objtest = test.getJSONObject(test.length() - j);
                    Log.i("ii", "" + (test.length()-j));
                    JSONObject objtest1 = objtest.getJSONObject("temp");
                    Log.i("ii", "" + objtest1.getDouble("day"));
                    Log.i("ii", "" + objtest1.getDouble("min"));
                    Log.i("ii", "" + objtest1.getDouble("max"));
                    Log.i("ii", "" + objtest1.getDouble("night"));

                    MoreObj.setDay(new java.util.Date(Long.parseLong(objtest.getString("dt"))));
                    MoreObj.setDay(new java.util.Date(Long.parseLong(objtest.getString("dt"))));
                    MoreObj.setAverageT((int) ((int) objtest1.getDouble("day") - 273.15));
                    MoreObj.setMinT((int) ((int) objtest1.getDouble("min") - 273.15));
                    MoreObj.setMaxT((int) ((int) objtest1.getDouble("max") - 273.15));
                    MoreObj.setNightT((int) ((int) objtest1.getDouble("night") - 273.15));

                    JSONArray test2=objtest.getJSONArray("weather");
                    JSONObject objtest2 = test2.getJSONObject(test2.length()-1);
                    Log.i("ii", "" + objtest2.getString("main"));
                    Log.i("ii", "" + objtest2.getString("description"));

                    MoreObj.setClouds(objtest2.getString("main"));
                    MoreObj.setDescription(objtest2.getString("description"));
                    MoreObj.setIcoID(WeatherIcon(objtest2.getString("icon")));

                    MCI.add(MoreObj);


                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            return null;
        }
    }
    public int WeatherIcon(String ico) {
        if(ico.equals("01d"))
        {
            return R.drawable.b13;
        }
        else if(ico.equals("01n"))
        {
            return R.drawable.b3;
        }
        else if(ico.equals("02d"))
        {
            return R.drawable.b5;
        }
        else if(ico.equals("02n"))
        {
            return R.drawable.b4;
        }
        else if((ico.equals("03d"))||ico.equals("04d")||ico.equals("03n")||ico.equals("04n"))
        {
            return R.drawable.b1;
        }
        else if((ico.equals("09d"))||(ico.equals("09n")))
        {
            return R.drawable.b2;
        }
        else if((ico.equals("10d"))||(ico.equals("10n")))
        {
            return R.drawable.b6;
        }
        else if((ico.equals("11d"))||(ico.equals("11n")))
        {
            return R.drawable.b12;
        }
        else if((ico.equals("13d"))||(ico.equals("13n")))
        {
            return R.drawable.b9;
        }
        else if((ico.equals("50d"))||(ico.equals("50n")))
        {
            return R.drawable.b14;
        }
        else
        {
            return R.drawable.error;
        }
    }

}