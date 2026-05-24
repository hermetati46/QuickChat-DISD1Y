package com.poepart1;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login auth = new Login();
        ArrayList<Message> sentMessagesList = new ArrayList<>();
        int totalMessagesSent = 0;

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
                                tempMsg.setNumMessagesSent(totalMessagesSent);

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
                                String hash = tempMsg.createMessageHash(id, totalMessagesSent, textContent);
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
                                    actionChoice = 2; // Padrão descarta se inserido incorretamente
                                }

                                String statusMsg = tempMsg.SentMessage(actionChoice);
                                System.out.println(statusMsg);

                                // It stores the message details in the JSON file
                                tempMsg.storeMessage(id, hash, destPhone, textContent, actionChoice);

                                if (actionChoice == 1) {
                                    totalMessagesSent++;
                                    sentMessagesList.add(tempMsg);
                                    System.out.println("\n# Message Details #");
                                    System.out.println(tempMsg.printMessages());
                                } else if (actionChoice == 3) {
                                    System.out.println("Message saved in store directory.");
                                }
                            }
                            break;

                        case 2:
                            System.out.println("Coming Soon.");
                            break;

                        case 3:
                            running = false;
                            System.out.println("Total messages sent during this session: " + totalMessagesSent);
                            System.out.println("Goodbye!");
                            break;

                        default:
                            System.out.println("Invalid option. Please choose 1, 2, or 3.");
                    }
                }
            }
        }
    }
}