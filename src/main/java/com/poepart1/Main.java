package com.poepart1;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login auth = new Login();
        ArrayList<Message> sentMessagesList = new ArrayList<>();
        int totalMessagesSent = 0;

        // Reads pre-existing data from JSON messages if it exists
        Message.readJSONFileToArray();

        System.out.println("# Registration #");
        System.out.print("Enter First Name: ");
        String fName = input.nextLine();
        System.out.print("Enter Last Name: ");
        String lName = input.nextLine();
        System.out.print("Enter Username: ");
        String user = input.nextLine();
        System.out.print("Enter Password: ");
        String pass = input.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = input.nextLine();

        String regStatus = auth.registerUser(user, pass, fName, lName, phone);
        System.out.println(regStatus);

        // Proceeds only if registration is successful
        if (regStatus.contains("successfully captured")) {
            System.out.println("\n# Login #");
            System.out.print("Enter Username: ");
            String loginUser = input.nextLine();
            System.out.print("Enter Password: ");
            String loginPass = input.nextLine();

            boolean loginResult = auth.loginUser(loginUser, loginPass);
            System.out.println(auth.returnLoginStatus(loginResult));

            // The user can only interact if successfully logged in
            if (loginResult) {
                System.out.println("\n# Welcome to QuickChat #");
                boolean running = true;

                while (running) {
                    System.out.println("\nPlease choose one of the following options:");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");
                    System.out.print("Your choice: ");

                    String choiceStr = input.nextLine();
                    int menuChoice = 0;
                    try {
                        menuChoice = Integer.parseInt(choiceStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        continue;
                    }

                    switch (menuChoice) {
                        case 1:
                            System.out.print("How many messages do you wish to enter? ");
                            int numMsgs = 0;
                            try {
                                numMsgs = Integer.parseInt(input.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number. Returning to menu.");
                                break;
                            }

                            for (int i = 0; i < numMsgs; i++) {
                                System.out.println("\n# Entering Message " + (i + 1) + " of " + numMsgs + " #");
                                Message tempMsg = new Message();
                                tempMsg.setNumMessagesSent(Message.allMessagesList.size());

                                // Here we enter the recipient's cell number for then send a message
                                boolean cellValid = false;
                                String destPhone = "";
                                while (!cellValid) {
                                    System.out.print("Enter Recipient Cell Number (e.g., +27718693002): ");
                                    destPhone = input.nextLine();
                                    String validationResult = tempMsg.checkRecipientCell(destPhone);
                                    System.out.println(validationResult);
                                    if (validationResult.contains("successfully captured")) {
                                        cellValid = true;
                                    }
                                }
                                tempMsg.setRecipient(destPhone);

                                // Message input with length validation
                                boolean msgValid = false;
                                String textContent = "";
                                while (!msgValid) {
                                    System.out.print("Enter Message (max 250 characters): ");
                                    textContent = input.nextLine();
                                    String lengthResult = tempMsg.checkMessageLength(textContent);
                                    System.out.println(lengthResult);
                                    if (lengthResult.contains("ready to send")) {
                                        msgValid = true;
                                    }
                                }
                                tempMsg.setMessageContent(textContent);

                                // Automatic ID and Hash Creation
                                String id = tempMsg.getMessageId();
                                System.out.println("Message ID generated: " + id);
                                String hash = tempMsg.createMessageHash(id, Message.allMessagesList.size(), textContent);
                                System.out.println("Message Hash: " + hash);

                                // The user decides what action the message will take
                                System.out.println("\nChoose action for this message:");
                                System.out.println("1) Send Message");
                                System.out.println("2) Disregard Message");
                                System.out.println("3) Store Message to send later");
                                System.out.print("Your Choice: ");
                                int actionChoice = 0;
                                try {
                                    actionChoice = Integer.parseInt(input.nextLine());
                                } catch (NumberFormatException e) {
                                    actionChoice = 2;
                                }

                                String statusMsg = tempMsg.SentMessage(actionChoice);
                                System.out.println(statusMsg);

                                Message.populateArrays(tempMsg);

                                // It stores the message details in the JSON file
                                tempMsg.storeMessage(id, hash, destPhone, textContent, tempMsg.getStatus());

                                System.out.println("\n# Message Details Saved #");
                                System.out.println(tempMsg.printMessages());
                            }
                            break;

                        case 2:
                            System.out.println("Coming Soon.");
                            break;

                        case 3:
                            // Submenu with the requirements for stored messages operations
                            System.out.println("\n# Stored messages operations #");
                            System.out.println("a) Display the sender and recipient of all stored messages");
                            System.out.println("b) Display the longest stored message");
                            System.out.println("c) Search for a message ID and display corresponding recipient and message");
                            System.out.println("d) Search for all the messages stored for a particular recipient");
                            System.out.println("e) Delete a message using the message hash");
                            System.out.println("f) Display a report of all stored messages");
                            System.out.print("Choose sub-option (a-f): ");
                            String subChoice = input.nextLine().trim().toLowerCase();

                            switch (subChoice) {
                                case "a":
                                    System.out.println(Message.getSenderRecipientReport(auth.getFirstName() + " " + auth.getLastName()));
                                    break;
                                case "b":
                                    System.out.println("Longest Message Content: \"" + Message.getLongestStoredMessage() + "\"");
                                    break;
                                case "c":
                                    System.out.print("Enter Message ID to Search: ");
                                    String searchID = input.nextLine();
                                    System.out.println("Result: " + Message.searchByID(searchID));
                                    break;
                                case "d":
                                    System.out.print("Enter Recipient Cell Number to Search: ");
                                    String searchRep = input.nextLine();
                                    System.out.println("Result: " + Message.searchByRecipient(searchRep));
                                    break;
                                case "e":
                                    System.out.print("Enter Message Hash to Delete: ");
                                    String deleteHash = input.nextLine();
                                    System.out.println(Message.deleteByHash(deleteHash));
                                    break;
                                case "f":
                                    System.out.println(Message.getStoredMessagesReport());
                                    break;
                                default:
                                    System.out.println("Invalid sub-option.");
                            }
                            break;

                        case 4:
                            running = false;
                            System.out.println("Total messages stored/sent during this session: " + Message.allMessagesList.size());
                            System.out.println("Goodbye!");
                            break;

                        default:
                            System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
                    }
                }
            }
        }
    }
}