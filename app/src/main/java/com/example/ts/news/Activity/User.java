package com.example.ts.news.Activity;

public class User {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String qm;
    public User(int id, String username, String password, String nickname,String qm) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.qm=qm;
    }
    public User() {
        super();
        // TODO Auto-generated constructor stub
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getqm() {
        return qm;
    }

    public void setqm(String qm) {
        this.qm=qm;
    }
}
