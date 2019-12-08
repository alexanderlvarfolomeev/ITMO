package ru.itmo.wp.servlet;

class UserTextPair {
    private final String user;
    private final String text;
    UserTextPair(final String user, final String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }
}
