package com.alertuniversity.alertu;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity
{

    private SwipeRefreshLayout swipeLayout;
    private ListView l;
    private ArrayList<Alert> returnArray;
    private ListView theListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is needed to have a custom action bar layout
        final android.app.ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        setContentView(R.layout.activity_main);

        ArrayList<Alert> alerts = new ArrayList<>();
        populateListView();
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.main);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        populateListView();
                        swipeLayout.setRefreshing(false);
                    }
                },2000);

            }
        });
    }

    public void populateListView()
    {
        //Create a list of alerts from the server
        getTheAlerts();
        ArrayList<Alert> alerts = returnArray;

        ListAdapter theAdapter = new MyAdapter(this, R.layout.row_layout, alerts);
        theListView = (ListView) findViewById(R.id.theListView);

        theListView.setAdapter(theAdapter);
    }

    public void getTheAlerts()
    {
        GetAlerts getAlerts = new GetAlerts();
        getAlerts.execute("http://alertu.net63.net/AlertU/connect.php");
        returnArray = new ArrayList<Alert>();

        try
        {
            getAlerts.get();
        }
        catch (Exception e)
        {
            //do something
            returnArray = new ArrayList<Alert>();
        }
    }

    private class GetAlerts extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... strings)
        {
            String result;
            InputStream is;
            try
            {
                URL au = new URL(strings[0]);
                URLConnection ac = au.openConnection();
                is = ac.getInputStream();

                Log.e("log_tag", "connection success");
                //   Toast.makeText(getApplicationContext(), “pass”, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection" + e.toString());
                Toast.makeText(getApplicationContext(), "Connection fail" + e.toString(), Toast.LENGTH_SHORT).show();
                return null;
            }
            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    //  Toast.makeText(getApplicationContext(), “Input Reading pass”, Toast.LENGTH_SHORT).show();
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result" + e.toString());
                Toast.makeText(getApplicationContext(), "Input reading fail", Toast.LENGTH_SHORT).show();
                return null;
            }

            try
            {
                ArrayList<Alert> testArray = new ArrayList<Alert>();

                if (!result.isEmpty()) {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Alert a = new Alert(jsonObject);
                        returnArray.add(a);
                        testArray.add(a);
                    }
                }
                return null;
            }
            catch (JSONException e)
            {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                return null;
            }
        }
    }

}
