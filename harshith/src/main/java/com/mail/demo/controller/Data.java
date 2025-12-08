package com.mail.demo.controller;

import com.mail.demo.controller.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

////////// time headers

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
///////////scheduled headers

import java.util.ArrayList;
import java.util.Optional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class Data {
    private static ArrayList<User> users = new ArrayList<>(); // list of users
    private static ArrayList<Inbox> inboxes = new ArrayList<>();

    // -----------------------------------------------------------------------------------------------
    // prady
    private static final Path USERS_FILE = Path.of("data/users.json");
    private static final Path INBOX_FILE = Path.of("data/inbox.json");

    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public <T> void writeToJson(Path path, ArrayList<T> list) throws Exception {
        mapper.writeValue(path.toFile(), list);
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
            setUnique();

        } catch (Exception e) {
            e.printStackTrace();
            users = new ArrayList<>();
            inboxes = new ArrayList<>();
        }
    }
    // -------------------------------------------------------------------------------------
    // // prady

    private static ArrayList<Mail> junkMail = new ArrayList<>();

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        Data.users = users;
        try {
            writeToJson(USERS_FILE, users);
        } catch (Exception ignored) {
        } // prady
    }

    // public ArrayList<Inbox> getInboxes() {
    // return inboxes;
    // }
    public static ArrayList<Mail> getJunkMail() {
        return junkMail;
    }

    public ArrayList<Mail> showInbox(String Gmail) {
        for (Inbox i : inboxes) {
            if (i.getEmail().equals(Gmail)) {
                ArrayList<Mail> mails = i.getMails();
                // sorting inbox for IMPORTANT mails first --- prady
                mails.sort((a, b) -> b.getImportance().compareTo(a.getImportance()));
                return mails;
            }
        }
        // System.out.println("No Mails in Inbox");
        return null; // if possible use exception and return a print statement
    }

    public void setInboxes(ArrayList<Inbox> inboxes) {
        Data.inboxes = inboxes;
        try {
            writeToJson(INBOX_FILE, inboxes);
        } catch (Exception ignored) {
        } // prady
    }

    public void addUser(User user) {
        users.add(user);
        try {
            writeToJson(USERS_FILE, users);
        } catch (Exception ignored) {
        } // prady
    }

    public void addInbox(Inbox inbox) {
        inboxes.add(inbox);
        try {
            writeToJson(INBOX_FILE, inboxes);
        } catch (Exception ignored) {
        } // prady
    }

    public String sendMail(Mail body, String gmail) {
        ZonedDateTime indiaTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd/MM/yyyy HH:mm");

        body.setTime(indiaTime.format(formatter));

        if (body == null || gmail == null) {
            return "Invalid request: missing mail or sender";
        }

        String sentBy = body.getSentby();
        String sendTo = body.getSendto();
        if (sentBy == null || sendTo == null) {
            return "Invalid mail: missing from/to fields";
        }

//        // Optional: ensure provided gmail matches the mail's sender
//        if (!gmail.equalsIgnoreCase(sentBy)) {
//            return "Sender mismatch";
//        }

        // 1) Check if sender is flagged as spammer (mark mail spam if so)
        for (User u : users) {
            if (u != null && u.getEmail() != null && u.getEmail().equalsIgnoreCase(sentBy) && u.isSpammer()) {
                body.setSpamMail(true);
                break;
            }
        }

        for(Inbox i : inboxes){
            if(i.getEmail() != null && (i.getEmail()).equals(body.getSentby())){
                i.addSentMail(body);
                try {writeToJson(INBOX_FILE, inboxes); } catch (Exception ignored) {}  //prady
            }
        }

        for(Inbox i : inboxes){
            if(i.getEmail() != null && (i.getEmail()).equals(body.getSendto())){
                if (body.isSpamMail()) {
                    i.addSpamMail(body);
                    try {writeToJson(INBOX_FILE, inboxes);} catch (Exception ignored) {}  //prady
                    return "Sent to Spam Mail";
                } else {
                    i.addMail(body);
                    try {writeToJson(INBOX_FILE, inboxes);} catch (Exception ignored) {}  //prady
                    return "Sent Successfully";
                }
            }
        }

        return "Please provide valid receivers mail!";

    }

    public boolean userExists(User user) {
        for (User u : users) {
            if (u.getEmail() != null && (u.getEmail()).equals(user.getEmail()))
                return true;
        }
        return false;
    }

    public boolean validateLogin(Templogin user) { // check if the password is correct
        for (User u : users) {
            if (u.getEmail() != null && (u.getEmail()).equals(user.getEmail())
                    && (u.getPassword()).equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    //////////////////////////////////////////////////////////// pranav

    public String deleteMail(String Gmail, int id) {
        for (Inbox i : inboxes) {
            if (i.getEmail().equals(Gmail)) {
                ArrayList<Mail> mails = i.getMails();
                for (int idx = 0; idx < mails.size(); idx++) {
                    Mail m = mails.get(idx);
                    if (m.getId() == id) {
                        junkMail.add(m);
                        mails.remove(idx);
                        try {
                            writeToJson(INBOX_FILE, inboxes);
                        } catch (Exception ignored) {}
                        return "Deleted successfully";
                    }
                }
                return "Invalid ID!!";
            }
        }
        return "Invalid Gmail!!";
    }


    public String deleteUser(String Gmail, String password) {
        for (int x = 0; x < users.size(); x++) {
            User u = users.get(x);

            if (u.getEmail().equals(Gmail) && u.getPassword().equals(password)) {

                users.remove(x);

                for (int y = 0; y < inboxes.size(); y++) {
                    Inbox i = inboxes.get(y);
                    if (i.getEmail().equals(Gmail)) {
                        inboxes.remove(y);
                        break;
                    }
                }

                try {
                    writeToJson(USERS_FILE, users);
                } catch (Exception ignored) {}

                try {
                    writeToJson(INBOX_FILE, inboxes);
                } catch (Exception ignored) {}

                return "User deleted successfully";
            }
        }
        return "Invalid Gmail or Password!";
    }



    ////////////////////////////////////////////////////////////
    public ArrayList<DraftMail> showDrafts(String draftmail) {
        for (Inbox i : inboxes) {
            if (i.getEmail().equals(draftmail)) {
                try {
                    writeToJson(INBOX_FILE, inboxes);
                } catch (Exception ignored) {
                }
                return i.getDraftMails();
            }
        }
        // System.out.println("No Mails in Inbox");
        return null; // if possible use exception and return a print statement
    }

    public String saveDraftMail(DraftMail body) {
        for (Inbox i : inboxes) {
            if (i.getEmail() != null && (i.getEmail()).equals(body.getSentby())) {
                i.addDraftMail(body);
                try {
                    writeToJson(INBOX_FILE, inboxes);
                } catch (Exception ignored) {
                }
                return "Saved Successfully";
            }
        }
        return "Please provide valid receivers mail!";
    }
    public String deleteDraft(String gmail, int id) {
        for (Inbox i : inboxes) {
            if (i.getEmail().equals(gmail)) {

                ArrayList<DraftMail> drafts = i.getDraftMails();

                for (int x = 0; x < drafts.size(); x++) {
                    DraftMail d = drafts.get(x);

                    if (d.getId() == id) {
                        drafts.remove(x);

                        try {
                            writeToJson(INBOX_FILE, inboxes);
                        } catch (Exception ignored) {}

                        return "Draft deleted successfully";
                    }
                }
                return "Draft not found";
            }
        }
        return "Draft not found";
    }


    public String editDraft(int id, String from, DraftUpdateRequest update) {
        for (Inbox i : inboxes) {

            if (i.getEmail().equals(from)) {

                for (DraftMail d : i.getDraftMails()) {
                    if (d.getId() == id) {
                        if (update.getSubject() != null) {
                            d.setSubject(update.getSubject());
                        }
                        if (update.getBody() != null) {
                            d.setBody(update.getBody());
                        }
                        if (update.getSendTo() != null) {
                            d.setSendto(update.getSendTo());
                        }
                        try {
                            writeToJson(INBOX_FILE, inboxes);
                        } catch (Exception ignored) {}

                        return "Draft edited successfully";
                    }
                }

            }
        }

        return "Draft not found";
    }

    public String sendDraftmail(DraftMail body) {
        Mail m = body.send();
        String s = body.getSendto();
        return sendMail(m, s);
    }

    public DraftMail GetDraftMail(int to, String from) {
        for (Inbox i : inboxes) {
            if (i.getEmail().equals(from)) {
                for (DraftMail j : i.getDraftMails()) {
                    if (j.getId()==to)
                        return j;
                }
            }
        }
        return null;
    }

    public String archiveMail(String gmail, int id) {
        for (Inbox inbox : inboxes) {
            if (inbox.getEmail().equals(gmail)) {

                ArrayList<Mail> mails = inbox.getMails();

                for (int x = 0; x < mails.size(); x++) {
                    Mail m = mails.get(x);

                    if (m.getId() == id) {
                        inbox.addToArchive(m);
                        mails.remove(x);
                        return "Mail archived successfully!";
                    }
                }

                return "Invalid Mail ID";
            }
        }
        return "Invalid Gmail!";
    }


    public ArrayList<Mail> showArchive(String gmail) {
        for (Inbox inbox : inboxes) {
            if (inbox.getEmail().equals(gmail)) {
                return inbox.getArchivedMails();
            }
        }
        return null;
    }

    public String unarchiveMail(String gmail, int id) {
        for (Inbox i : inboxes) {
            if (i.getEmail().equals(gmail)) {
                ArrayList<Mail> archived = i.getArchivedMails();
                ArrayList<Mail> mails = i.getMails();
                for (Mail m : archived) {
                    if (m.getId() == id) {
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

    public String scheduleMail(ScheduledMail m, String Gmail) {
        for (Inbox i : inboxes) {
            if (i.getEmail().equals(Gmail)) {
                i.addScheduledMail(m);
                try {
                    writeToJson(INBOX_FILE, inboxes);
                } catch (Exception ignored) {
                }
                return "Scheduled your Mail successfully";
            }
        }
        return "Something went wrong";
    }

    public static ArrayList<ScheduledMail> showScheduledMails(String Gmail) {
        for (Inbox i : inboxes) {
            if (i.getEmail().equals(Gmail)) {
                return i.getScheduledMails();
            }
        }
        return null;
    }

    public void checkAndSendScheduledMails() {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        LocalDateTime nowLocal = now.toLocalDateTime();

        DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE dd/MM/yyyy HH:mm");

        for (Inbox inbox : inboxes) {

            ArrayList<ScheduledMail> list = inbox.getScheduledMails();
            if (list == null)
                continue;

            ArrayList<ScheduledMail> toRemove = new ArrayList<>();

            for (ScheduledMail s : list) {
                try {
                    LocalDateTime scheduled = LocalDateTime.parse(s.getScheduledTime(), f);

                    if (!scheduled.isAfter(nowLocal)) {

                        // THREADING PART
                        new Thread(() -> sendMail(s, s.getSendto())).start();

                        toRemove.add(s);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to parse: " + s.getScheduledTime());
                }
            }

            list.removeAll(toRemove);
        }

        // try {writeToJson(INBOX_FILE, inboxes); } catch (Exception ignored) {}
    }

    @Scheduled(fixedRate = 60000) // runs every 1 min
    public void scheduledMailChecker() {
        checkAndSendScheduledMails();
    }

    public ArrayList<Mail> showJunk(String Gmail){
        for (Inbox i: inboxes){
            if(i.getEmail().equals(Gmail)){
                return i.getJunkMail();
            }
        }
        return null;
    }
    public ArrayList<Mail> showSpam(String Gmail){
        for(Inbox i:inboxes){
            if(i.getEmail().equals(Gmail))
            {
                return i.getSpam();
            }
        }
        return null;
    }
    public ArrayList<Mail> showSentMail(String Gmail){
        for(Inbox i:inboxes){
            if(i.getEmail().equals(Gmail))
            {
                return i.getSentMails();
            }
        }
        return null;
    }

    public String ReportSpam(String reportedMail, String gmail, int id) {

        boolean userFound = false;
        boolean mailFound = false;


        for (User u : users) {
            if (u.getEmail().equals(reportedMail)) {
                u.addReporter(gmail);
                userFound = true;
                break;
            }
        }


        for (Inbox inbox : inboxes) {
            if (inbox.getEmail().equals(gmail)) {

                for (Mail m : inbox.getMails()) {
                    if (m.getId() == id) {
                        inbox.addSpamMail(m);
                        m.setSpamMail(true);
                        inbox.removeMail(m);
                        mailFound = true;
                        break;
                    }
                }

                break;
            }
        }


        if (!userFound) return "Reported user not found!";
        if (!mailFound) return "Mail ID not found in user's inbox!";
        try {
            writeToJson(USERS_FILE, users);
            writeToJson(INBOX_FILE, inboxes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Spam reported successfully.";
    }
    public void setUnique(){
        int j = -1;
        for(Inbox inbox : inboxes ){
            for(Mail m : inbox.getMails()){
                if(m.getId() > j) j = m.getId();
            }
            for(Mail m : inbox.getSentMails()){
                if(m.getId() > j) j = m.getId();
            }
            for(Mail m : inbox.getArchivedMails()){
                if(m.getId() > j) j = m.getId();
            }
            for(Mail m : inbox.getSpam()){
                if(m.getId() > j) j = m.getId();
            }
        }
        Mail.setUnique(j + 1);
    }////

    public String unSendMail(int id, String from){
        int i=0;
        for(Inbox inbox : inboxes ){
            for(Mail m : inbox.getMails()){
                if(m.getSentby().equals(from) && m.getId()==id){
                    inbox.getMails().remove(m);
                    i++;
                }

            }
            for(Mail m : inbox.getSentMails()){
                if(m.getSentby().equals(from) && m.getId()==id){
                    inbox.getSentMails().remove(m);
                    i++;
                }

            }

        }
        if(i==2)return "true";
        return "failed";
    }
    public ArrayList<Mail> searchMails(String gmail, String keyword) {

        keyword = keyword.toLowerCase();

        ArrayList<Mail> result = new ArrayList<>();

        for (Inbox i : inboxes) {

            if (i.getEmail().equals(gmail)) {

                // search inbox mails
                for (Mail m : i.getMails()) {
                    if (matches(m, keyword)) {
                        result.add(m);
                    }
                }

                // search sent mails
                for (Mail m : i.getSentMails()) {
                    if (matches(m, keyword)) {
                        result.add(m);
                    }
                }

                return result;  // done for this user
            }
        }

        return result;
    }
    private boolean matches(Mail m, String keyword) {
        if (m.getSubject() != null &&
                m.getSubject().toLowerCase().contains(keyword))
            return true;

        if (m.getBody() != null &&
                m.getBody().toLowerCase().contains(keyword))
            return true;

        if (m.getSentby() != null &&
                m.getSentby().toLowerCase().contains(keyword))
            return true;

        if (m.getSendto() != null &&
                m.getSendto().toLowerCase().contains(keyword))
            return true;

        return false;
    }
}


