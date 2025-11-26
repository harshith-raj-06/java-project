package com.mail.demo.controller;

public class DraftMail extends Mail{
    public boolean sent=false;//maybe add some note to the draft

    public Mail send(){
        if(!sent) {
            Mail mail = new Mail();
            mail.setBody(getBody());
            mail.setSendto(getSendto());
            mail.setSentby(getSentby());
            mail.setTime(getTime());
            mail.setSubject(getSubject());
            sent=true;
            return mail;
        }
        return null;
    }
}