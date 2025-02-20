package model;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.Nullable;

public class Users {
    private String user_name;
    private String password;
    private String email;

    private String loginType;
    public Users() {
    }

    public Users(String user_name, String password, String email, String loginType) {
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        this.loginType = loginType;
    }
    public Users(String user_name, String email) {
        this.user_name = user_name;
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
