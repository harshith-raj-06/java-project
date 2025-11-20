package com.mail.demo.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Data {
    private static ArrayList<User> users = new ArrayList<>(); //list of users
    private static ArrayList<Inbox> inboxes = new ArrayList<>();

   // ----------------------------------------------------------------------------------------------- prady
    private static final Path USERS_FILE = Path.of("data/users.json");
    private static final Path INBOX_FILE = Path.of("data/inbox.json");

    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public <T> void writeToJson(Path path , ArrayList<T> list) throws Exception{
        mapper.writeValue(path.toFile(),list);
    }

    public <T> ArrayList<T> readFromJson(Path path, TypeReference<ArrayList<T>> typeRef) throws Exception {
        String json = Files.readString(path);
        return mapper.readValue(json, typeRef);
    }

    public Data() {
        try {
            if (!USERS_FILE.toFile().exists()) {
                USERS_FILE.toFile().getParentFile().mkdirs();
                Files.writeString(USERS_FILE, "[]");
            }
            if (!INBOX_FILE.toFile().exists()) {
                INBOX_FILE.toFile().getParentFile().mkdirs();
                Files.writeString(INBOX_FILE, "[]");
            }
            users = readFromJson(USERS_FILE, new TypeReference<ArrayList<User>>() {
            });
            inboxes = readFromJson(INBOX_FILE, new TypeReference<ArrayList<Inbox>>() {
            });

        } catch (Exception e) {
            users = new ArrayList<>();
            inboxes = new ArrayList<>();
        }
    }
// ------------------------------------------------------------------------------------- // prady
    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        Data.users = users;
        try { writeToJson(USERS_FILE, users); } catch (Exception ignored) {} // prady
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
        try { writeToJson(INBOX_FILE, inboxes); } catch (Exception ignored) {}  //prady
    }

    public void addUser(User user){
        users.add(user);
        try { writeToJson(USERS_FILE,users); } catch (Exception ignored) {}  //prady
    }

    public void addInbox(Inbox inbox){
        inboxes.add(inbox);
        try { writeToJson(INBOX_FILE, inboxes); } catch (Exception ignored) {}  //prady
    }

    public String sendMail(Mail body, String Gmail){
        for(Inbox i : inboxes){
            if(i.getEmail() != null && (i.getEmail()).equals(body.getSendto())){
                i.addMail(body);
                try {writeToJson(INBOX_FILE, inboxes); } catch (Exception ignored) {}  //prady
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
