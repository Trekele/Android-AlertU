package com.alertuniversity.alertu;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private SwipeRefreshLayout swipeLayout;
    private Handler handler = new Handler();
    private ListView theListView;
    private ArrayList<Alert> returnArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This is needed to have a custom action bar layout
        final android.app.ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.action_bar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        setContentView(R.layout.activity_main);

        populateListView();

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.main);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateListView();
                swipeLayout.setRefreshing(false);
            }
        });

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: Display alert screen
                /*
                Alert idk = (Alert) adapterView.getItemAtPosition(i);
                Intent getAlert = new Intent(MainActivity.this, DisplayAlertScreen.class);

                final int result = 1;

                getAlert.putExtra("Alert", idk);
                startActivity(getAlert);
                */
            }
        });

    }

    public void populateListView()
    {
        //Create a list of alerts from the server
        getTheAlerts();
        List<Alert> alerts = returnArray;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
