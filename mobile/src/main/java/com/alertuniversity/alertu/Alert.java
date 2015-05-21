package com.alertuniversity.alertu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Trekele on 4/23/15.
 */

//class needs to implement the Serializable interface in order to be able to
//be passed between activities
public class Alert  implements Serializable {

    //alert attributes
    private String alertText;
    private double alertLng;
    private double alertLat;
    private String alertTitle;
    private Date alertDate;
    private double alertRadius;
    private int alertPriority;
    private int alertId;



    public Alert(String alertTitle, String alertText, double alertLng,
                 double alertLat, Date alertDate, double alertRadius, int alertPriority, int alertId)
    {
        this.alertTitle = alertTitle;
        this.alertText = alertText;
        this.alertLng = alertLng;
        this.alertLat = alertLat;
        this.alertDate = alertDate;
        this.alertRadius = alertRadius;
        this.alertPriority = alertPriority;
        this.alertId = alertId;
    }

    public Alert(JSONObject jsonObject)
    {
        try
        {
            this.alertTitle = jsonObject.getString("title");
            this.alertText = jsonObject.getString("description");
            this.alertLat = jsonObject.getDouble("latitude");
            this.alertLng = jsonObject.getDouble("longitude");
            this.alertRadius = jsonObject.getDouble("radius");
            this.alertPriority = jsonObject.getInt("priority");
            this.alertDate = new SimpleDateFormat("yyyy-MM-d HH:mm:ss").parse(jsonObject.getString("createdAt"));
            this.alertId = jsonObject.getInt("id");
        }
        //if an exception is ever thrown at any point during this process, then the JSON is not formatted
        //correctly and needs this code or the JSON will need to be changed
        catch(Exception e)
        {
            //do something
            e = new JSONException("JSON NOT IN RIGHT FORMAT");
        }
    }

    public Alert()
    {

    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public double getAlertRadius() {
        return alertRadius;
    }

    public void setAlertRadius(double alertRadius) {
        this.alertRadius = alertRadius;
    }

    public int getAlertPriority() {
        return alertPriority;
    }

    public void setAlertPriority(int alertPriority) {
        this.alertPriority = alertPriority;
    }

    public String getAlertText() {
        return alertText;
    }

    public void setAlertText(String alertText) {
        this.alertText = alertText;
    }

    public double getAlertLng() {
        return alertLng;
    }

    public void setAlertLng(double alertLng) {
        this.alertLng = alertLng;
    }

    public double getAlertLat() {
        return alertLat;
    }

    public void setAlertLat(double alertLat) {
        this.alertLat = alertLat;
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }
}