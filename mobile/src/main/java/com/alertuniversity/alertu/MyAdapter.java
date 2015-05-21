package com.alertuniversity.alertu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by Trekele on 4/23/15.
 */
public class MyAdapter extends ArrayAdapter<Alert>
{
    public MyAdapter(Context context, List<Alert> alerts)
    {
        super(context, R.layout.row_layout, alerts);
    }

    public MyAdapter(Context context, int resource, List<Alert> alerts)
    {
        super(context, resource, alerts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout, parent, false);

        Alert alert = getItem(position);

        TextView tvAlertTitle = (TextView)theView.findViewById(R.id.tvAlertTitle);
        TextView tvAlertText = (TextView)theView.findViewById(R.id.tvAlertText);
        TextView tvAlertTime = (TextView)theView.findViewById(R.id.tvAlertTime);

        tvAlertTitle.setText(alert.getAlertTitle());

        if (alert.getAlertText().length() > 55)
        {
            tvAlertText.setText(alert.getAlertText().substring(0,55) + "...");
        }
        else
        {
            tvAlertText.setText(alert.getAlertText());
        }

        //tvAlertTime.setText(alert.getAlertDate().toString());
        tvAlertTime.setText(formatDateToCheese(alert.getAlertDate()));


        return theView;
    }

    public String formatDateToCheese(Date alertDate)
    {
        String returnString = "";
        Date currentDate = new Date();
        alertDate.getTime();

        double timeSinceMin = (double)(currentDate.getTime()-alertDate.getTime())/1000/60;

        if (timeSinceMin > 1)
        {
            double timeSinceHour = timeSinceMin/60;

            if (timeSinceHour > 1)
            {
                double timeSinceDay = timeSinceHour / 24;
                if(timeSinceDay > 1)
                {
                    double timeSinceWeek = timeSinceDay / 7;
                    if (timeSinceWeek > 1)
                    {
                        returnString = alertDate.toString();
                    }
                    else
                    {
                        returnString = (int) timeSinceDay == 1 ? (int) timeSinceDay + " day ago" : (int) timeSinceDay + " days ago";
                    }

                }
                else
                {
                    returnString = (int) timeSinceHour == 1 ? (int) timeSinceHour + " hour ago" : (int) timeSinceHour + " hours ago";
                }

            }
            else
            {
                returnString = (int) timeSinceMin + " min Ago";
            }
        }
        else
        {
            returnString = "Just now";
        }

        return returnString;
    }
}