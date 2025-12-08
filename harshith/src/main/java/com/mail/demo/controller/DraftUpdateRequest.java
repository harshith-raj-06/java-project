package com.mail.demo.controller;

public class DraftUpdateRequest {
    private String subject;
    private String body;
    private String sendTo;

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

     public String getSendTo() {
         return sendTo;
     }

     public void setSendTo(String sendTo) {
         this.sendTo = sendTo;
     }
 }
