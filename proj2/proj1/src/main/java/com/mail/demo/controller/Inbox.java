package com.mail.demo.controller;

import java.util.ArrayList;

public class Inbox{
    private String email;
    private ArrayList<Mail> mails = new ArrayList<>();
    private ArrayList<DraftMail> draftmails = new ArrayList<>();
    private ArrayList<Mail> archivedMails=new ArrayList<>();
    private ArrayList<ScheduledMail> scheduledMails = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Mail> getMails() {
        return mails;
    }
    public void setMails(ArrayList<Mail> mails) {
        this.mails = mails;    // IMPORTANT for JSON read
    }
    public ArrayList<DraftMail> getDraftMails() { return draftmails; }
    public void setDraftMails(ArrayList<DraftMail> draftmails) {
        this.draftmails = draftmails;   // IMPORTANT for JSON read
    }

    public void addMail(Mail m) {
        mails.add(m);
    }

    public void addDraftMail(DraftMail M){
        draftmails.add(M);
    }

    public ArrayList<Mail> getArchivedMails(){
        return archivedMails;
    }

    public void addToArchive(Mail m){
        archivedMails.add(m);
    }
    public ArrayList<ScheduledMail> getScheduledMails() {
        return scheduledMails;
    }

    public void addScheduledMail(ScheduledMail m) {
        scheduledMails.add(m);
    }
}