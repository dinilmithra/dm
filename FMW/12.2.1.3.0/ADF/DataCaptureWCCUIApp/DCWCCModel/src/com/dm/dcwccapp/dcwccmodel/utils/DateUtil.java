package com.dm.dcwccapp.dcwccmodel.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class DateUtil {

    public static Date ucmDateToUtilDate(String strDate) {

        Date date = null;

        if ((strDate != null) && !strDate.trim().isEmpty()) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'");
            //        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            //            String myDateStr = "2017-07-18 05:29:50Z";
            try {
                date = sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    public static String utilDateToUcmDate(Date date) {

        String strDate = "";

        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'");
            strDate = sdf.format(date);
        }

        return strDate;
    }
}
