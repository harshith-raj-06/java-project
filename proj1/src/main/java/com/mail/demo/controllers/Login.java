package com.mail.demo.controllers;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.*;

@RestController    //check this out
@RequestMapping("/loginPage")
public class Login{
    Data data = new Data();

    @GetMapping("/dob/{name}")
    public String showDOB(@PathVariable String name){
//        for(User u : users){
//            if(u.getName() != null && (u.getName()).equals(name)) return u.getDOB();
//        }

        //System.out.println("No Mails in Inbox");
        return "User doesn't exist";
    }

    @PostMapping ("/login")
    public String loginlist(@RequestBody Templogin temp){
        if(data.validateLogin(temp)) return "Login successful...";

        return "Login failed mail id or password is incorrect ";
    }

    @PostMapping("/createUser")
    public String createAccount(@RequestBody User user){
        if(data.userExists(user)) return "User already exists!";

        EDGE checker = new EDGE();
        if(checker.validateFullPassword(user.getPassword(),user.getEmail()).equals("logging in.....")) {    //checking if the password is valid
            data.addUser(user);

            Inbox new_inbox = new Inbox();
            new_inbox.setEmail(user.getEmail());
            data.addInbox(new_inbox);

            return "User created successfully😊";
        }

        return checker.validateFullPassword(user.getPassword(),user.getEmail()); //createUser failed as password is invalid
    }

    @GetMapping("/getUsers")
    public ArrayList<User> showUsers(){
        return data.getUsers();
    }

    //send mail
    @PostMapping("/{Gmail}/sendMail")
    public String sendMail(@RequestBody Mail m, @PathVariable String Gmail){
        return data.sendMail(m, Gmail);
    }

    @GetMapping("/{Gmail}/showInbox")
    public ArrayList<Mail> showInbox(@PathVariable String Gmail){
        return data.showInbox(Gmail);
    }
}

                                                //sentby and sendto
class Mail{
    private String sentby;
    private String sendto;
    private String subject;
    private String body;
    private String time;

    public String getSendto() {
        return sendto;
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

}

class Inbox{
    private String email;
    private ArrayList<Mail> mails = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Mail> getMails() {
        return mails;
    }

    public void addMail(Mail m) {
        mails.add(m);
    }

}