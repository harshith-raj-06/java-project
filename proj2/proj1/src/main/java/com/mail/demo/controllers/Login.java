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

    ///////////////////////////////////////////////////////////////////pranav
    @GetMapping("/getJunk")
    public ArrayList<Mail> showJunk(){return data.getJunkMail();}
    @DeleteMapping("/{Gmail}/deleteMail")
    public String deleteMail(@PathVariable String Gmail, @RequestBody Templogin i){          //enter id in raw Text
        return data.deleteMail(Gmail, i.getId());
    }

    @DeleteMapping("/{Gmail}/deleteUser")
    public String deleteUser(@PathVariable String Gmail, @RequestBody Templogin password){  //enter password in raw Text
        return data.deleteUser(Gmail, password.getPassword());
    }
    //////////////////////////////////////////////////////////////////

    @PostMapping("/{Gmail}/saveDraft")
    public String SaveDraft(@RequestBody DraftMail d, @PathVariable String Gmail){
        return data.saveDraftMail(d);

    }
    @GetMapping("/{Gmail}/showDraft")
    public ArrayList<DraftMail> showDraft(@PathVariable String Gmail){
        return data.showDrafts(Gmail);
    }


    /// use mail id instead of temp2 & temp3 object and also in "/{Gmail}/deleteMail"
    /// also delete the draft from drafts arraylist
    @PostMapping("/{Gmail}/sendDraft")
    public String SendDraft(@RequestBody Templogin a, @PathVariable String Gmail){

        DraftMail temp=data.GetDraftMail(a.getName(),Gmail);
        if(temp != null){
            return data.sendDraftmail(temp);
        }
        return  "failed";

    }

    @PutMapping("/{Gmail}/archiveMail/{id}")
    public String archiveMail(@PathVariable String Gmail,@PathVariable int id){
        return data.archiveMail(Gmail,id);
    }

    @GetMapping("/{Gmail}/showArchive")
    public ArrayList<Mail> showArchive(@PathVariable String Gmail){
        return data.showArchive(Gmail);
    }

    @PutMapping("/{Gmail}/unarchiveMail/{id}")
    public String unarchiveMail(@PathVariable String Gmail,@PathVariable int id){
        return data.unarchiveMail(Gmail,id);
    }
}

//sentby and sendto
class Mail{
    private String sentby;
    private String sendto;
    private String subject;
    private String body;
    private String time;
    private static int unique = 0;
    private int id;

    Mail(){
        id=unique++;
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

}

class DraftMail extends Mail{
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

class Inbox{
    private String email;
    private ArrayList<Mail> mails = new ArrayList<>();
    private ArrayList<DraftMail> draftmails = new ArrayList<>();
    private ArrayList<Mail> archivedMails=new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Mail> getMails() {
        return mails;
    }
    public ArrayList<DraftMail> getDraftMails() { return draftmails; }

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
}
