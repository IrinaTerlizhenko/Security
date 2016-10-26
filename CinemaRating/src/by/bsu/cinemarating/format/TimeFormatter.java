package by.bsu.cinemarating.format;

import java.sql.Timestamp;

/**
 * Created by User on 24.06.2016.
 */
public class TimeFormatter {
    /**
     * @param datetime date and time in format mm/dd/yyyy hh:mm AM(PM)
     * @return date and time in Timestamp format yyyy-mm-dd hh:mm:ss
     */
    public static Timestamp format(String datetime) {
        String[] parts = datetime.split("[/ :]");
        String hours = parts[3];
        if ("PM".equals(parts[5])) {
            hours = String.valueOf(Integer.parseInt(hours) + 12);
        }
        String timestamp = parts[2] + "-" + parts[0] + "-" + parts[1] + " " + hours + ":" + parts[4] + ":00";
        return Timestamp.valueOf(timestamp);
    }
}
