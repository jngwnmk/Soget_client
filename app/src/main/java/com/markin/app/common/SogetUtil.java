package com.markin.app.common;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wonmook on 15. 5. 9..
 */
public class SogetUtil {

    public static String calDurationTimeForComment(long pastTime){
        String result = "";
//        String dateStart = "01/14/2012 09:29:58";
//        String dateStop = "01/15/2012 10:31:48";

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        //long d1 = null;
        Date d2 = null;

        try {
            Date currentTime = new Date();

            //d1 = pastTime;//format.parse(format.format(pastTime));
            d2 = format.parse(format.format(currentTime));

            //in milliseconds
            long diff = d2.getTime() - pastTime;

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if(diffDays>0){
                Date date = new Date(pastTime);
                Format commentformat = new SimpleDateFormat("yyyy-MM-dd");
                result = commentformat.format(date);
            } else {
                if(diffHours>0){
                    result = diffHours+" hours";
                } else {
                    if(diffMinutes>0){
                        result = diffMinutes +" mins";
                    } else {
                        result = "now";
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String calDurationTime(long pastTime){
        String result = "";
//        String dateStart = "01/14/2012 09:29:58";
//        String dateStop = "01/15/2012 10:31:48";

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        //long d1 = null;
        Date d2 = null;

        try {
            Date currentTime = new Date();

            //d1 = pastTime;//format.parse(format.format(pastTime));
            d2 = format.parse(format.format(currentTime));

            //in milliseconds
            long diff = d2.getTime() - pastTime;

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if(diffDays>0){
                result = diffDays+"d";
            } else {
                if(diffHours>0){
                    result = diffHours+"h";
                } else {
                    if(diffMinutes>0){
                        result = diffMinutes +"m";
                    } else {
                        result = "now";
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
