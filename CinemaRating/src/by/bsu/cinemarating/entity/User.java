package by.bsu.cinemarating.entity;

import java.sql.Date;

public class User extends Entity {
    private String login;
    private String email;
    private Date regDate;
    private byte roleID;
    private String name;
    private String surname;
    private double status;
    private String password;
    private String photo;
    private int numRated;

    public User() {
    }

    public User(int id, String login, String email, Date regDate, byte roleID, String name, String surname, double status, String photo, int numRated) {
        super(id);
        this.login = login;
        this.email = email;
        this.regDate = regDate;
        this.roleID = roleID;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.photo = photo;
        this.numRated = numRated;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public byte getRoleID() {
        return roleID;
    }

    public void setRoleID(byte roleID) {
        this.roleID = roleID;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getNumRated() {
        return numRated;
    }

    public void setNumRated(int numRated) {
        this.numRated = numRated;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", regDate=" + regDate +
                ", roleID=" + roleID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", status=" + status +
                ", photo='" + photo + '\'' +
                ", numRated=" + numRated +
                '}';
    }
}
