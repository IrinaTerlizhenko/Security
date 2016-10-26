package by.bsu.cinemarating.format;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Wrapper around java.sql.Timestamp to display time in format "YYYY-MM-dd HH:mm:ss"
 */
public class FormattedTimestamp {
    private Timestamp timestamp;

    public FormattedTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(timestamp);
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
