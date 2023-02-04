package de.littleprogrammer.lpmcbans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampCalculator {

    public Long calculateDifference(String date1, String date2, String value) {
        Timestamp date_1 = stringToTimestamp(date1);
        Timestamp date_2 = stringToTimestamp(date2);
        long milliseconds = date_1.getTime() - date_2.getTime();
        switch (value) {
            case "second":
                return milliseconds / 1000;
            case "minute":
                return milliseconds / 1000 / 60;
            case "hours":
                return milliseconds / 1000 / 3600;
            default:
                return new Long(999999999);
        }
    }

    public Timestamp stringToTimestamp(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(date);
            return new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            return null;
        }
    }


}
