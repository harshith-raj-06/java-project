package com.mail.demo.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

public class Data {
    private static ArrayList<User> users = new ArrayList<>(); //list of users
    private static ArrayList<Inbox> inboxes = new ArrayList<>();

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        Data.users = users;
    }

//    public ArrayList<Inbox> getInboxes() {
//        return inboxes;
//    }

    public ArrayList<Mail> showInbox(String Gmail){
        for(Inbox i : inboxes){
            if(i.getEmail().equals(Gmail)){
                return i.getMails();
            }
        }
        //System.out.println("No Mails in Inbox");
        return null;  //if possible use exception and return a print statement
    }

    public void setInboxes(ArrayList<Inbox> inboxes) {
        Data.inboxes = inboxes;
    }

    public void addUser(User user){
        users.add(user);
    }

    public void addInbox(Inbox inbox){
        inboxes.add(inbox);
    }

    public String sendMail(Mail body, String Gmail){
        for(Inbox i : inboxes){
            if(i.getEmail() != null && (i.getEmail()).equals(body.getSendto())){
                i.addMail(body);
                return "Sent Successfully";
            }
        }
        return "Please provide valid receivers mail!";
    }

    public boolean userExists(User user){
        for(User u : users){
            if(u.getEmail() != null && (u.getEmail()).equals(user.getEmail())) return true;
        }
        return false;
    }

    public boolean validateLogin(Templogin user){   //check if the password is correct
        for(User u : users){
            if(u.getEmail() != null && (u.getEmail()).equals(user.getEmail()) && (u.getPassword()).equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

}
