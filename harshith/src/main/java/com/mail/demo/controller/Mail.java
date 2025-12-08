package com.mail.demo.controller;

import java.util.concurrent.atomic.AtomicInteger;

//sentby and sendto
public class Mail {
    private String sentby;
    private String sendto;
    private String subject;
    private String body;
    private Importance importance = Importance.DEFAULT; // importance --- prady
    private String time;
    private static AtomicInteger unique = new AtomicInteger(1);
    private int id;
    private boolean isSpamMail = false;

    Mail() {
        id = unique.getAndIncrement();
    }

    public String getSendto() {
        return sendto;
    }

    public int getId() {
        return id;
    }

    public void setSendto(String sendto) {
        this.sendto = sendto;
    }

    public String getSentby() {
        return sentby;
    }

    public void setSentby(String sentby) {
        this.sentby = sentby;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public boolean isSpamMail() {
        return isSpamMail;
    }

    public void setSpamMail(boolean spamMail) {
        isSpamMail = spamMail;
    }

    public static void setUnique(int n) {
        unique.set(n);
    }////
}
