package com.sapp.glet.GameRequests;

import java.util.Date;

/**
 * Created by Backlash on 29.03.2017.
 */

public class Time {
    // calculates the time difference between current time and time from timePicker in minutes
    public static int calculateTimeDifference(int newHour, int newMins){
        Date curTime = new Date(); // currentTime
        int curHour = curTime.getHours() + 2;
        int curMins = curTime.getMinutes();

        int newTimeInMins = (newHour * 60) + newMins;
        int curTimeInMins = (curHour * 60) + curMins;
        int difTime;
        int fullDayInMin = 1440;

        if(newTimeInMins >= curTimeInMins){
            difTime = newTimeInMins - curTimeInMins;
        }else{
            difTime = (fullDayInMin - curTimeInMins) + newTimeInMins;
        }

        return difTime;
    }
}