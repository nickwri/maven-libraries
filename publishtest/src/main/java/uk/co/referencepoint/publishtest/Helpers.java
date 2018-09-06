package uk.co.referencepoint.publishtest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helpers {

    public static String getCurrentDateStringPlusMinutes(int minutesToAdd, String format) {

        Date dt = new Date(System.currentTimeMillis() + (minutesToAdd * 60 * 1000));
        return getDateString(dt, format);
    }

    public static String getDateString(Date date, String format) {
        if (date == null) return "";
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            return fmt.format(date);
        } catch (Exception pe) {
            return "";
        }
    }
}
