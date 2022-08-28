package com.example.traveler.model;

public class User {

    private int userId;
    private String email;
    private String password;
    private int roleId;
    private String accessToken;

    public User(){
    }

    // Alt + Ins
    public User(int userId, String email, String password, int roleId, String accessToken) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.accessToken = accessToken;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return this.roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
