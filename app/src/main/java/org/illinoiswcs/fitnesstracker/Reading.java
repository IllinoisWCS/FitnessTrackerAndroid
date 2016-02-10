package org.illinoiswcs.fitnesstracker;

/**
 * Created by Corly on 2/9/2016.
 */
public class Reading {
    private String date;
    private float rate;
    private String time;

    public Reading(){
        //Default
    }

    public Float getRate() {
        return rate;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
