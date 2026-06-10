package com.poepart1;

import java.util.regex.Pattern;

public class Login {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        boolean hasCap = !password.equals(password.toLowerCase());
        boolean hasNum = password.matches(".*\\d.*");
        boolean hasSpecial = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE).matcher(password).find();

        return password.length() >= 8 && hasCap && hasNum && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String phone) {
        return phone != null && phone.startsWith("+27") && phone.length() == 12 && phone.substring(1).matches("\\d+");
    }

    public String registerUser(String username, String password, String firstName, String lastName, String phone) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(phone)) {
            return "Phone number is not correctly formatted; please ensure it starts with +27 and contains 12 characters in total.";
        }

        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phone;

        return "\nUsername successfully captured.\nPassword successfully captured.\nPhone number successfully captured.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(this.username) && enteredPassword.equals(this.password);
    }

    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}