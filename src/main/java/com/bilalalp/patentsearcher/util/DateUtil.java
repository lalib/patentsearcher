package com.bilalalp.patentsearcher.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    public static final String DD_MM_YY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";

    private DateUtil() {
        // Util Class
    }

    public static String dateToString(final Date date, final String pattern) {
        if (date != null) {
            final DateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        } else {
            return null;
        }
    }

    public static long getDateDiff(final Date startDate, final Date endDate) {

        if (startDate != null && endDate != null) {
            final Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            final long startDateMiliSeconds = startCalendar.getTimeInMillis();

            final Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            final long endDateMiliSeconds = endCalendar.getTimeInMillis();
            final long result = (endDateMiliSeconds - startDateMiliSeconds) / 1000;
            if (result > 1) {
                return result;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}