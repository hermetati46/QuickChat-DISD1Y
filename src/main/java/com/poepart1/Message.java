package com.poepart1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Message {
    private String messageId;
    private int numMessagesSent;
    private String recipient;
    private String messageContent;
    private String messageHash;
    private String status;

    // Global lists that mimic parallel arrays for storing message details.
    public static ArrayList<String> sentMessages = new ArrayList<>();
    public static ArrayList<String> disregardedMessages = new ArrayList<>();
    public static ArrayList<String> storedMessages = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> messageIDs = new ArrayList<>();

    // Complete list of objects to facilitate complex searches
    public static ArrayList<Message> allMessagesList = new ArrayList<>();

    //Construtor
    public Message() {
        this.messageId = generateRandomMessageID();
    }

    public Message(String messageId, String recipient, String messageContent, String status, int numSent) {
        this.messageId = messageId;
        this.recipient = recipient;
        this.messageContent = messageContent;
        this.status = status;
        this.numMessagesSent = numSent;
        this.messageHash = createMessageHash(messageId, numSent, messageContent);

        populateArrays(this);
    }

    // Method for populating parallel arrays
    public static void populateArrays(Message msg) {
        allMessagesList.add(msg);
        messageIDs.add(msg.getMessageId());
        messageHashes.add(msg.getMessageHash());

        if ("Sent".equalsIgnoreCase(msg.getStatus())) {
            sentMessages.add(msg.getMessageContent());
        } else if ("Disregard".equalsIgnoreCase(msg.getStatus())) {
            disregardedMessages.add(msg.getMessageContent());
        } else if ("Stored".equalsIgnoreCase(msg.getStatus())) {
            storedMessages.add(msg.getMessageContent());
        }
    }

    // Helper to generate 10-digit ID
    private String generateRandomMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    // Validate the message ID (no more than 10 characters)
    public boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

    // Reuses and validates the recipient's mobile phone number
    public String checkRecipientCell(String phone) {
        if (phone != null && phone.startsWith("+27") && phone.length() == 12 && phone.substring(1).matches("\\d+")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Creates the message hash according to the POE instructions
    public String createMessageHash(String id, int num, String msg) {
        if (id == null || id.length() < 2 || msg == null || msg.trim().isEmpty()) {
            return "00:" + num + ":ERROR";
        }

        String firstTwo = id.substring(0, Math.min(2, id.length()));
        String trimmedMsg = msg.trim();
        String[] words = trimmedMsg.split("\\s+");

        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        this.messageHash = firstTwo + ":" + num + ":" + firstWord + lastWord;
        return this.messageHash;
    }

    // Validates the text size (no more than 250 characters)
    public String checkMessageLength(String msg) {
        if (msg == null) {
            return "Please enter a message of less than 250 characters.";
        }
        if (msg.length() <= 250) {
            return "Message ready to send.";
        } else {
            int exceeded = msg.length() - 250;
            return "Message exceeds 250 characters by " + exceeded + "; please reduce the size.";
        }
    }

    // Returns the appropriate message status based on the user's choice
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                this.status = "Sent";
                return "Message successfully sent.";
            case 2:
                this.status = "Disregard";
                return "Press 0 to delete the message.";
            case 3:
                this.status = "Stored";
                return "Message successfully stored.";
            default:
                this.status = "Disregard";
                return "Invalid choice.";
        }
    }

    // Formats the message display data
    public String printMessages() {
        return "Message ID: " + this.messageId + "\n" +
                "Message Hash: " + this.messageHash + "\n" +
                "Recipient: " + this.recipient + "\n" +
                "Message: " + this.messageContent;
    }

    // It stores the messages details in a JSON file with the appropriate format
    public boolean storeMessage(String id, String hash, String rep, String msg, String currentStatus) {
        try {
            String jsonFormat = "{\n" +
                    "  \"MessageID\": \"" + id + "\",\n" +
                    "  \"MessageHash\": \"" + hash + "\",\n" +
                    "  \"Recipient\": \"" + rep + "\",\n" +
                    "  \"Message\": \"" + msg + "\",\n" +
                    "  \"Status\": \"" + currentStatus + "\"\n" +
                    "}";

            File file = new File("messages_store.json");
            FileWriter writer = new FileWriter(file, true);
            writer.write(jsonFormat + "\n---\n");
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Reads the simulated data in JSON format to populate the program's lists
    public static void readJSONFileToArray() {
        try {
            File file = new File("messages_store.json");
            if (!file.exists()) return;

            String content = new String(Files.readAllBytes(Paths.get("messages_store.json")));
            String[] records = content.split("---");

            for (String rec : records) {
                if (rec.trim().isEmpty()) continue;
                String id = extractJSONField(rec, "MessageID");
                String hash = extractJSONField(rec, "MessageHash");
                String rep = extractJSONField(rec, "Recipient");
                String msg = extractJSONField(rec, "Message");
                String stat = extractJSONField(rec, "Status");

                if (id != null && !messageIDs.contains(id)) {
                    Message m = new Message(id, rep, msg, stat, allMessagesList.size());
                }
            }
        } catch (Exception e) {
            // Silent if file is empty or inaccessible in the build
        }
    }
    private static String extractJSONField(String json, String field) {
        try {
            String pattern = "\"" + field + "\": \"";
            int start = json.indexOf(pattern) + pattern.length();
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }

    // a) Displays sender and recipient of saved messages
    public static String getSenderRecipientReport(String sender) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Stored messages Sender and Recipient #\n");
        boolean found = false;
        for (Message m : allMessagesList) {
            if ("Stored".equalsIgnoreCase(m.getStatus())) {
                sb.append("Sender: ").append(sender).append(" -> Recipient: ").append(m.getRecipient()).append("\n");
                found = true;
            }
        }
        if (!found) sb.append("No stored messages available.\n");
        return sb.toString();
    }

    // b) Returns the text of the longest message that was stored
    public static String getLongestStoredMessage() {
        Message longest = null;
        for (Message m : allMessagesList) {
            if ("Stored".equalsIgnoreCase(m.getStatus())) {
                if (longest == null || m.getMessageContent().length() > longest.getMessageContent().length()) {
                    longest = m;
                }
            }
        }
        if (longest != null) {
            return longest.getMessageContent();
        }
        return "No stored messages available.";
    }

    // c) Search by Message ID and return recipient and text
    public static String searchByID(String id) {
        for (Message m : allMessagesList) {
            if (m.getMessageId().equals(id)) {
                return m.getMessageContent();
            }
        }
        return "Message ID not found.";
    }

    // d) Search all messages from a specific recipient (Sent ou Stored)
    public static String searchByRecipient(String rep) {
        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (Message m : allMessagesList) {
            if (m.getRecipient().equals(rep) && ("Sent".equalsIgnoreCase(m.getStatus()) || "Stored".equalsIgnoreCase(m.getStatus()))) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(m.getMessageContent());
                found = true;
            }
        }
        if (!found) return "No messages found for this recipient.";
        return sb.toString();
    }

    // e) Delete a message using its Hash
    public static String deleteByHash(String hash) {
        for (int i = 0; i < allMessagesList.size(); i++) {
            Message m = allMessagesList.get(i);
            if (m.getMessageHash().equalsIgnoreCase(hash)) {
                allMessagesList.remove(i);
                messageIDs.remove(m.getMessageId());
                messageHashes.remove(hash);
                if ("Sent".equalsIgnoreCase(m.getStatus())) {
                    sentMessages.remove(m.getMessageContent());
                } else if ("Stored".equalsIgnoreCase(m.getStatus())) {
                    storedMessages.remove(m.getMessageContent());
                } else if ("Disregard".equalsIgnoreCase(m.getStatus())) {
                    disregardedMessages.remove(m.getMessageContent());
                }
                return "Message: \"" + m.getMessageContent() + "\" successfully deleted.";
            }
        }
        return "Hash not found.";
    }

    // f) Display a report that lists the full details of all the stored messages
    public static String getStoredMessagesReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("# Stored messages report #\n");
        boolean found = false;
        for (Message m : allMessagesList) {
            if ("Stored".equalsIgnoreCase(m.getStatus())) {
                sb.append("Hash: ").append(m.getMessageHash()).append("\n")
                        .append("Recipient: ").append(m.getRecipient()).append("\n")
                        .append("Message: ").append(m.getMessageContent()).append("\n")
                        .append("-----------------------------\n");
                found = true;
            }
        }
        if (!found) sb.append("No stored messages found.");
        return sb.toString();
    }

    //Getters and Setters
    public String getMessageId() { return messageId; }
    // public void setMessageId(String messageId) { this.messageId = messageId; }

    // public int getNumMessagesSent() { return numMessagesSent; }
    public void setNumMessagesSent(int numMessagesSent) { this.numMessagesSent = numMessagesSent; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getMessageContent() { return messageContent; }
    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }

    public String getMessageHash() { return messageHash; }

    public String getStatus() { return status; }
}