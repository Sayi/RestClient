package com.deepoove.restclient.model;

import com.deepoove.restclient.util.StringUtils;

import java.io.Serializable;

/**
 * @author Sayi
 */

public class BasicAuth implements Serializable {
    private String username;
    private String password;

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

    public boolean isValid() {
        return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
    }
}
