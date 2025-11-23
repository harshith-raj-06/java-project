package com.project.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class MailController {

    @Autowired
    private Data data;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (data.userExists(user)) return "User already exists!";
        data.addUser(user);
        return "User registered successfully!";
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@RequestParam String email, @RequestParam String password) {
        return data.deleteUser(email, password);
    }

    @PostMapping("/mail/send")
    @PreAuthorize("authentication.name == #mail.sentby")
    public String sendMail(@RequestBody Mail mail) {
        return data.sendMail(mail, mail.getSendto());
    }

    @GetMapping("/inbox/{email}")
    @PreAuthorize("authentication.name == #email")
    public ArrayList<Mail> getInbox(@PathVariable String email) {
        return data.showInbox(email);
    }

    @DeleteMapping("/mail/{email}/{id}")
    @PreAuthorize("authentication.name == #email")
    public String deleteMail(@PathVariable String email, @PathVariable int id) {
        return data.deleteMail(email, id);
    }


    @PostMapping("/drafts/save")
    @PreAuthorize("authentication.name == #draft.sentby")
    public String saveDraft(@RequestBody DraftMail draft) {
        return data.saveDraftMail(draft);
    }

    @GetMapping("/drafts/{email}")
    @PreAuthorize("authentication.name == #email")
    public ArrayList<DraftMail> getDrafts(@PathVariable String email) {
        return data.showDrafts(email);
    }

    @PostMapping("/drafts/send")
    @PreAuthorize("authentication.name == #draft.sentby")
    public String sendDraft(@RequestBody DraftMail draft) {
        return data.sendDraftmail(draft);
    }

    @PutMapping("/mail/archive/{email}/{id}")
    @PreAuthorize("authentication.name == #email")
    public String archiveMail(@PathVariable String email, @PathVariable int id) {
        return data.archiveMail(email, id);
    }

    @GetMapping("/archives/{email}")
    @PreAuthorize("authentication.name == #email")
    public ArrayList<Mail> getArchives(@PathVariable String email) {
        return data.showArchive(email);
    }

    @PutMapping("/mail/unarchive/{email}/{id}")
    @PreAuthorize("authentication.name == #email")
    public String unarchiveMail(@PathVariable String email, @PathVariable int id) {
        return data.unarchiveMail(email, id);
    }
}