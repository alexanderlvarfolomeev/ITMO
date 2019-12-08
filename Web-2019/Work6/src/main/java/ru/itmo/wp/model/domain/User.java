package ru.itmo.wp.model.domain;

import java.io.Serializable;

public class User extends Entity implements Serializable {
    private String login;
    private String email;
    private boolean admin = false;

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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
