package com.mail.demo.controllers;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
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

    public Optional<User> findUserByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return users.stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst();
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

    private static ArrayList<Mail> junkMail= new ArrayList<>();

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
public static ArrayList<Mail> getJunkMail() {
    return junkMail;
}




    public ArrayList<Mail> showInbox(String Gmail){
        for(Inbox i : inboxes){
            if(i.getEmail().equals(Gmail)){
                ArrayList<Mail> mails = i.getMails();
                // sorting inbox for IMPORTANT mails first --- prady
                mails.sort((a, b) -> b.getImportance().compareTo(a.getImportance()));
                return mails;
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
                break;
            }
        }
        for(Inbox i : inboxes){
            if(i.getEmail() != null && (i.getEmail()).equals(body.getSentby())){
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

    ////////////////////////////////////////////////////////////pranav
    public String deleteMail(String Gmail, int id){
        for(Inbox i : inboxes){
            if(i.getEmail().equals(Gmail)){
                for(Mail m : i.getMails()){
                    if(m.getId() == id){
                        junkMail.add(m);
                        i.getMails().remove(m);
                        return "Deleted successfully";
                    }
                }
                return "Invalid ID!!";
            }
        }
        return "Invalid Gmail!!";
    }

    public String deleteUser(String Gmail, String password){
        for(User u : users){
            if((u.getEmail()).equals(Gmail) && (u.getPassword()).equals(password)) {
                users.remove(u);
                return "User deleted successfully";
            }
        }
        return "Invalid Gmail or Password!";
    }
    ////////////////////////////////////////////////////////////
    public ArrayList<DraftMail> showDrafts(String draftmail){
        for(Inbox i : inboxes){
            if(i.getEmail().equals(draftmail)){
                return i.getDraftMails();
            }
        }
        //System.out.println("No Mails in Inbox");
        return null;  //if possible use exception and return a print statement
    }

    public String saveDraftMail(DraftMail body){
        for(Inbox i : inboxes){
            if(i.getEmail() != null && (i.getEmail()).equals(body.getSentby())){
                i.addDraftMail(body);
                return "Saved Successfully";
            }
        }
        return "Please provide valid receivers mail!";
    }
    public String sendDraftmail(DraftMail body){
        Mail m= body.send();
        String s= body.getSendto();
        return sendMail(m,s);
    }

    public DraftMail GetDraftMail(String to, String from){
        for(Inbox i : inboxes ){
            if(i.getEmail().equals(from)){
                for(DraftMail j : i.getDraftMails()){
                    if(j.getSendto().equals(to))return j;
                }
            }
        }
        return null;
    }

    public String archiveMail(String gmail,int id){
        for(Inbox inbox : inboxes){
            if(inbox.getEmail().equals(gmail)){
                ArrayList<Mail> mails=inbox.getMails();
                for(Mail m:mails){
                    if(m.getId()==id){
                        inbox.addToArchive(m);
                        mails.remove(m);
                        return "Mail archived successfully!";
                    }
                }
                return "Invalid Mail ID";
            }
        }
        return "Invalid Gmail!";
    }

    public ArrayList<Mail> showArchive(String gmail){
        for(Inbox inbox:inboxes){
            if(inbox.getEmail().equals(gmail)){
                return inbox.getArchivedMails();
            }
        }
        return null;
    }

    public String unarchiveMail(String gmail,int id){
        for(Inbox i:inboxes){
            if(i.getEmail().equals(gmail)){
                ArrayList<Mail> archived=i.getArchivedMails();
                ArrayList<Mail> mails=i.getMails();
                for(Mail m: archived){
                    if(m.getId()==id){
                        archived.remove(m);
                        mails.add(m);
                        return "Mail unarchived successfully!";
                    }
                }
                return "Invalid Mail ID!";
            }
        }
        return "Invalid Gmail!";
    }
}
