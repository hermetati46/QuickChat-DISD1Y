package com.poepart1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    Login login = new Login();
    Message message = new Message();

    @BeforeEach
    public void setUp() {
        // Clear the static lists before each test to avoid contamination
        Message.allMessagesList.clear();
        Message.sentMessages.clear();
        Message.disregardedMessages.clear();
        Message.storedMessages.clear();
        Message.messageHashes.clear();
        Message.messageIDs.clear();

        // Message 1
        new Message("01", "+27834557896", "Did you get the cake?", "Sent", 0);
        // Message 2
        new Message("02", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored", 1);
        // Message 3
        new Message("03", "+27834484567", "Yohoooo, I am at your gate.", "Disregard", 2);
        // Message 4
        new Message("0838884567", "+27838884567", "It is dinner time !", "Sent", 3);
        // Message 5
        new Message("05", "+27838884567", "Ok, I am leaving without you.", "Stored", 4);
    }

    @Test
    // Verifies that a valid username is accepted
    public void testUsernameCorrect() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    // Verifies that an invalid username is rejected
    public void testUsernameIncorrect() {
        assertFalse(login.checkUserName("kyle!!!!!!"));
    }

    // Verifies and accepts a password that meets all the required rules
    @Test
    public void testPasswordComplexitySuccess() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    // Verifies and rejects a password that doesn't meet the required rules
    @Test
    public void testPasswordComplexityFailure() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testRegistrationMessaging() {
        // Successful registration case
        String success = login.registerUser("kyl_1", "Ch&&sec@ke99!", "John", "Doe", "+27838968976");
        assertTrue(success.contains("Username successfully captured."));
        assertTrue(success.contains("Password successfully captured."));
        assertTrue(success.contains("Phone number successfully captured."));

        // Phone failure case
        String failPhone = login.registerUser("kyl_1", "Ch&&sec@ke99!", "John", "Doe", "0123456789");
        assertEquals("Phone number is not correctly formatted; please ensure it starts with +27 and contains 12 characters in total.", failPhone);
    }

    @Test
    // Verifies that a message with a valid length is accepted
    public void testMessageLengthSuccess() {
        // Message success test <= 250 characters
        String validMsg = "Hi Mike, can you join us for dinner tonight?";
        assertEquals("Message ready to send.", message.checkMessageLength(validMsg));
    }

    @Test
    // Verifies that a message with an invalid length is rejected
    public void testMessageLengthFailure() {
        // Message failure test > 250 characters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            sb.append("a");
        }
        String invalidMsg = sb.toString();
        String expectedError = "Message exceeds 250 characters by 10; please reduce the size.";
        assertEquals(expectedError, message.checkMessageLength(invalidMsg));
    }

    @Test
    public void testRecipientNumberSuccess() {
        // Tests Valid recipient
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testRecipientNumberFailure() {
        // Tests Invalid recipient
        String expectedError = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        assertEquals(expectedError, message.checkRecipientCell("08575975889"));
    }

    @Test
    public void testMessageHashCreation() {
        // Hash Test
        String hashResult = message.createMessageHash("0012345678", 0, "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", hashResult);
    }

    @Test
    public void testSentMessageOptions() {
        // Validating return values based on menu selection
        assertEquals("Message successfully sent.", message.SentMessage(1));
        assertEquals("Press 0 to delete the message.", message.SentMessage(2));
        assertEquals("Message successfully stored.", message.SentMessage(3));
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        // O sistema deve retornar as mensagens registradas como "Sent" (1 e 4)
        assertEquals(2, Message.sentMessages.size());
        assertEquals("Did you get the cake?", Message.sentMessages.get(0));
        assertEquals("It is dinner time !", Message.sentMessages.get(1));
    }

    @Test
    public void testDisplayLongestMessage() {
        String longest = Message.getLongestStoredMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    @Test
    public void testSearchByID() {
        // Searching for Message 4 with ID "0838884567" should return "It is dinner time!"
        String msgContent = Message.searchByID("0838884567");
        assertEquals("It is dinner time !", msgContent);
    }

    @Test
    public void testSearchAllMessagesByRecipient() {
        // Search for all messages with status Sent or Stored for the recipient "+27838884567"
        String results = Message.searchByRecipient("+27838884567");
        assertTrue(results.contains("Where are you?"));
        assertTrue(results.contains("It is dinner time !"));
        assertTrue(results.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteByHash() {
        // Delete Message 2 based on the automatically generated Hash for it
        String hashTarget = Message.allMessagesList.get(1).getMessageHash();
        String deleteResult = Message.deleteByHash(hashTarget);

        assertTrue(deleteResult.contains("successfully deleted"));

        assertFalse(Message.messageHashes.contains(hashTarget));
    }
}