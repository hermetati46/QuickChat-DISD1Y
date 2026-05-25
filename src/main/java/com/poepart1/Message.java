package com.poepart1;

import java.util.Random;

public class Message {
    private String messageId;
    private int numMessagesSent;
    private String recipient;
    private String messageContent;
    private String messageHash;

    //Construtor
    public Message() {
        this.messageId = generateRandomMessageID();
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

        String firstTwo = id.substring(0, 2);
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
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return "Message successfully stored.";
            default:
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

    // It stores the messages details in Markdown format to a JSON file
    public boolean storeMessage(String id, String hash, String rep, String msg, int statusChoice) {
        try {
            String jsonFormat = "{\n" +
                    "  \"MessageID\": \"" + id + "\",\n" +
                    "  \"MessageHash\": \"" + hash + "\",\n" +
                    "  \"Recipient\": \"" + rep + "\",\n" +
                    "  \"Message\": \"" + msg + "\",\n" +
                    "  \"StatusChoice\": " + statusChoice + "\n" +
                    "}";

            java.io.File file = new java.io.File("messages_store.json");
            java.io.FileWriter writer = new java.io.FileWriter(file, true);
            writer.write(jsonFormat + "\n,\n");
            writer.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Getters and Setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public int getNumMessagesSent() { return numMessagesSent; }
    public void setNumMessagesSent(int numMessagesSent) { this.numMessagesSent = numMessagesSent; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getMessageContent() { return messageContent; }
    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }

    public String getMessageHash() { return messageHash; }
}