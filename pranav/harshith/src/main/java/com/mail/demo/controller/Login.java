package com.mail.demo.controller;

import java.security.Principal;
import java.util.ArrayList;

import com.mail.demo.controller.*;
import com.mail.demo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController    //check this out
@RequestMapping("/wemail")
public class Login{
    private final Data data;

    public Login(Data data) {
        this.data = data;
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
//    @GetMapping("sendMail/help")
//    public String helperSendMail(){
//        return "{\n" +
//                "    \"sentby\": \"bhar@gmail.com\",\n" +
//                "    \"sendto\":\"bhar11@gmail.com\",\n" +
//                "    \"subject\": \"Draftmail\",\n" +
//                "    \"body\":\"this is a draftmail\",\n" +
//                "    \"time\":\"15:29\"\n" +
//                "}";
//    }

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
    @GetMapping("deleteMail/help")
    public String helperdeleteMail(){
        return "provide id of the mail to delete\n" +
                " 'id' :int id ";
    }
    @DeleteMapping("/{Gmail}/deleteMail")
    public String deleteMail(@PathVariable String Gmail, @RequestBody Templogin i, Principal principal){          //enter id in raw Text
        assertOwnership(Gmail, principal);
        return data.deleteMail(Gmail, i.getId());
    }
    @GetMapping("deleteUser/help")
    public String helperdeleteUser(){
        return "provide password of the User to delete\n" +
                " 'password' :password ";
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
    @PutMapping("/{Gmail}/editDraft/{id}")
    public String editDraft(
            @PathVariable String Gmail,
            @PathVariable int id,
            @RequestBody DraftUpdateRequest update,
            Principal principal) {

        assertOwnership(Gmail, principal);
        return data.editDraft(id, Gmail, update);
    }
    @DeleteMapping("/{Gmail}/deleteDraft/{id}")
    public String deleteDraft(
            @PathVariable String Gmail,
            @PathVariable int id,
            Principal principal) {

        assertOwnership(Gmail, principal);
        return data.deleteDraft(Gmail, id);
    }



    /// use mail id instead of temp2 & temp3 object and also in "/{Gmail}/deleteMail"
    /// also delete the draft from drafts arraylist
    @PostMapping("/{Gmail}/sendDraft")
    public String SendDraft(@RequestBody Templogin a, @PathVariable String Gmail, Principal principal){
        assertOwnership(Gmail, principal);

        DraftMail temp=data.GetDraftMail(a.getId(),Gmail);
        if(temp.isSent()){
            return "Draft already Sent!";
        }

        if(temp != null){
            return data.sendDraftmail(temp);
        }

        return  "Draft does not exist";

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
    public String setScheduledMail(@RequestBody ScheduledMail m, @PathVariable String Gmail, Principal principal){
        assertOwnership(Gmail, principal);
        return data.scheduleMail(m, Gmail);
    }

    @GetMapping("/{Gmail}/showScheduledMails")
    public ArrayList<ScheduledMail> showScheduledMails(@PathVariable String Gmail,Principal principal){
        assertOwnership(Gmail, principal);
        return data.showScheduledMails(Gmail);
    }
    /////////////////////////////////////////////////
    @GetMapping("/{Gmail}/showSentMail")
    public ArrayList<Mail> showSent(@PathVariable String Gmail,Principal principal){
        assertOwnership(Gmail, principal);
        return data.showSentMail(Gmail);
    }

    @GetMapping("/{Gmail}/getJunk")
    public ArrayList<Mail> showJunk(@PathVariable String Gmail,Principal principal){
        assertOwnership(Gmail, principal);
        return data.showJunk(Gmail);
    }

    @GetMapping("/{Gmail}/getSpam")
    public ArrayList<Mail> showSpam(@PathVariable String Gmail,Principal principal){
        assertOwnership(Gmail, principal);
        return data.showSpam(Gmail);
    }

    @PostMapping("/{Gmail}/{id}/spamMail")
    public String Report(@RequestBody Templogin b,@PathVariable String Gmail,@PathVariable int id,Principal principal){
        assertOwnership(Gmail, principal);
        String ReportedMail=b.getEmail();
        if (ReportedMail == null) {
            return "Email cannot be null";
        }

        String result = data.ReportSpam(ReportedMail, Gmail, id);
        return result;
    }
    @PutMapping("/{Gmail}/unSend")
    public String unSendMail(@RequestBody Templogin b,@PathVariable String Gmail,Principal principal){
        assertOwnership(Gmail, principal);
        if(b.getId() ==0){
            return "Email cannot be null";
        }
        else {
            data.unSendMail(b.getId(), Gmail);
        }
        return "Successful";
    }
    @GetMapping("/{Gmail}/search")
    public ArrayList<Mail> search(@PathVariable String Gmail, @RequestParam String keyword, Principal principal) {

        assertOwnership(Gmail, principal);
        return data.searchMails(Gmail, keyword);
    }

    @GetMapping("/menu")
    public String showApiMenu() {        /// check all url properly

        return """
        ================== WEMAIL API MENU ==================
    
        AUTH & USER
        POST   /wemail/loginPage/createUser
        POST   /wemail/loginPage/login
        GET    /wemail/getUsers
        DELETE /wemail/{gmail}/deleteUser
    
        MAIL OPERATIONS
        POST   /wemail/{gmail}/sendMail
        GET    /wemail/{gmail}/showInbox
        DELETE /wemail/{gmail}/deleteMail
        GET    /wemail/getJunk
    
        DRAFT
        POST   /wemail/{gmail}/saveDraft
        GET    /wemail/{gmail}/showDraft
        POST   /wemail/{gmail}/sendDraft
    
        ARCHIVE
        PUT    /wemail/{gmail}/archiveMail/{id}
        GET    /wemail/{gmail}/showArchive
        PUT    /wemail/{gmail}/unarchiveMail/{id}
    
        SCHEDULED MAIL
        POST   /wemail/{gmail}/setScheduledMail
        GET    /wemail/{gmail}/showScheduledMails
    
        =====================================================
        """;
    }

}