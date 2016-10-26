package by.bsu.cinemarating.entity;

import by.bsu.cinemarating.format.FormattedTimestamp;

public class Review {
    private User user;
    private int mid;
    private String text;
    private FormattedTimestamp time;

    public Review(User user, int mid, String text, FormattedTimestamp time) {
        this.user = user;
        this.mid = mid;
        this.text = text;
        this.time = time;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FormattedTimestamp getTime() {
        return time;
    }

    public void setTime(FormattedTimestamp time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Review{" +
                "userId=" + user.getId() +
                ", movieId=" + mid +
                ", text='" + text + '\'' +
                ", time=" + time +
                '}';
    }
}
