package com.coderbunker.hyperledger.login;


public class User {
    private String login;
    private String pwd;
    private String affiliation;

    public User(String login, String pwd) {
        this.login = login;
        this.pwd = pwd;
        this.affiliation = "STUB_AFFILIATION"; // TODO complete with real data
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
