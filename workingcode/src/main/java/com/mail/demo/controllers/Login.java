package com.mail.demo.controllers;

import java.security.Principal;
import java.util.ArrayList;

import com.mail.demo.controller.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController    //check this out
@RequestMapping("/wemail")
public class Login{
    private final com.mail.demo.controller.Data data;

    public Login(com.mail.demo.controller.Data data) {
        this.data = data;
    }

    @GetMapping("/dob/{name}")
    public String showDOB(@PathVariable String name){
//        for(User u : users){
//            if(u.getName() != null && (u.getName()).equals(name)) return u.getDOB();
//        }

        //System.out.println("No Mails in Inbox");
        return "User doesn't exist";
    }

    @PostMapping ("/loginPage/login")
    public String loginlist(@RequestBody Templogin temp){
        if(data.validateLogin(temp)) return "Login successful...";

        return "Login failed mail id or password is incorrect ";
    }

    @PostMapping("/loginPage/createUser")
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
    public String sendMail(@RequestBody Mail m, @PathVariable String Gmail, Principal principal){
        assertOwnership(Gmail, principal);
        return data.sendMail(m, Gmail);
    }

    @GetMapping("/{Gmail}/showInbox")
    public ArrayList<Mail> showInbox(@PathVariable String Gmail, Principal principal){
        assertOwnership(Gmail, principal);
        return data.showInbox(Gmail);
    }

    ///////////////////////////////////////////////////////////////////pranav
    @GetMapping("/getJunk")
    public ArrayList<Mail> showJunk(){return data.getJunkMail();}
    @DeleteMapping("/{Gmail}/deleteMail")
    public String deleteMail(@PathVariable String Gmail, @RequestBody Templogin i, Principal principal){          //enter id in raw Text
        assertOwnership(Gmail, principal);
        return data.deleteMail(Gmail, i.getId());
    }

    @DeleteMapping("/{Gmail}/deleteUser")
    public String deleteUser(@PathVariable String Gmail, @RequestBody Templogin password, Principal principal){  //enter password in raw Text
        assertOwnership(Gmail, principal);
        return data.deleteUser(Gmail, password.getPassword());
    }
    //////////////////////////////////////////////////////////////////

    @PostMapping("/{Gmail}/saveDraft")
    public String SaveDraft(@RequestBody DraftMail d, @PathVariable String Gmail, Principal principal){
        assertOwnership(Gmail, principal);
        return data.saveDraftMail(d);

    }
    @GetMapping("/{Gmail}/showDraft")
    public ArrayList<DraftMail> showDraft(@PathVariable String Gmail, Principal principal){
        assertOwnership(Gmail, principal);
        return data.showDrafts(Gmail);
    }


    /// use mail id instead of temp2 & temp3 object and also in "/{Gmail}/deleteMail"
    /// also delete the draft from drafts arraylist
    @PostMapping("/{Gmail}/sendDraft")
    public String SendDraft(@RequestBody Templogin a, @PathVariable String Gmail, Principal principal){
        assertOwnership(Gmail, principal);

        DraftMail temp=data.GetDraftMail(a.getEmail(),Gmail);
        if(temp != null){
            return data.sendDraftmail(temp);
        }
        return  "failed";

    }

    @PutMapping("/{Gmail}/archiveMail/{id}")
    public String archiveMail(@PathVariable String Gmail,@PathVariable int id, Principal principal){
        assertOwnership(Gmail, principal);
        return data.archiveMail(Gmail,id);
    }

    @GetMapping("/{Gmail}/showArchive")
    public ArrayList<Mail> showArchive(@PathVariable String Gmail, Principal principal){
        assertOwnership(Gmail, principal);
        return data.showArchive(Gmail);
    }

    @PutMapping("/{Gmail}/unarchiveMail/{id}")
    public String unarchiveMail(@PathVariable String Gmail,@PathVariable int id, Principal principal){
        assertOwnership(Gmail, principal);
        return data.unarchiveMail(Gmail,id);
    }

    private void assertOwnership(String gmail, Principal principal) {
        if (principal == null || gmail == null || !gmail.equalsIgnoreCase(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }
    }
    //////////////////////////////////////////////////
    @PostMapping("/{Gmail}/setScheduledMail")
    public String setScheduledMail(@RequestBody ScheduledMail m, @PathVariable String Gmail){
        return data.scheduleMail(m, Gmail);
    }

    @GetMapping("/{Gmail}/showScheduledMails")
    public ArrayList<ScheduledMail> showScheduledMails(@PathVariable String Gmail){
        return data.showScheduledMails(Gmail);
    }
    /////////////////////////////////////////////////
}
