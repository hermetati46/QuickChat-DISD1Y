# Student Details
Name: Hermenegildo Tati

Student Number: ST10494935

Course: Diploma in Software Development (DISD0601)

PROG5121 - POE - Part 1 & Part 2

Github Repository: https://github.com/hermetati46/QuickChat-DISD1Y/

Youtube Link: https://youtu.be/MNGkKS8yMgw

# Overview

QuickChat is a console-based Java application developed as part of an academic Portfolio of Evidence (PoE). It implements a secure user authentication system (registration and login) alongside an interactive, validated message processing system. Users can compose, format, validate, hash, and persist message histories.

# Features

- User Registration with validated username, password, and phone number
- User Login with credential matching
- Message Composition with recipient validation, character limit enforcement, and auto-generated message IDs and hashes
- Message Actions: Send, Discard, or Store a message
- JSON Persistence of all message metadata to messages_store.json
- Unit Tests covering all core validation logic

# Architecture
The application is structured into three Java classes:

- Main - Application Entry Point

The entry point that wires everything together through a console menu. It runs a linear flow - registration must succeed before login is attempted, and login must succeed before the menu appears.

- Login - Registration & Authentication

Handles user registration and authentication. Contains validation rules ensuring all access credentials are valid before execution. It runs all checks and stores the fields if they pass, then does a straight equality check against those stored values.

- Message - Composition, Validation & Storage

Is the class responsible for managing the entire lifecycle of a message, from creation and validation to hashing and storage. It maps user actions like send, discard, or store to corresponding status messages, formats message details for display, and persists all message metadata to a JSON file

# Validation Rules
- Username
  - Must contain an underscore (_)
  - Must be no more than 5 characters in length
- Password
  - Minimum 8 characters
  - Must contain at least one uppercase letter
  - Must contain at least one number
  - Must contain at least one special character
- Phone Number (Registration & Recipient)
  - Must start with +27
  - Must be exactly 12 characters in total
  - All characters after + must be digits
- Message
  - Maximum 250 characters
  - Message ID must be no more than 10 digits (auto-generated)

# Screenshots

<img width="1920" height="1026" alt="Captura de tela 2026-05-24 223728" src="https://github.com/user-attachments/assets/b9520000-a4f2-45c7-bc01-f0fa2c833fa1" />

<img width="1920" height="1026" alt="Captura de tela 2026-05-24 224013" src="https://github.com/user-attachments/assets/a2819802-21d8-4874-b8d1-d0f74d3cf88c" />

# References

Deitel, P. and Deitel, H., 2017. Java How to Program, Early Objects. 11th ed. Boston: Pearson.

Flanagan, D., 2005. Java in a Nutshell. 5th ed. Sebastopol, CA: O'Reilly Media.

Oracle, 2024. The Java Tutorials: Lesson: Regular Expressions. [online] Available at: <https://docs.oracle.com/javase/tutorial/essential/regex/> [Accessed 24 May 2026].

Siewierski, C., 2015. An Introduction to Scholarship: Building Academic Skills for Tertiary Study. Oxford University Press Southern Africa.

W3Schools, 2026. Java User Input (Scanner). [online] Available at: <https://www.w3schools.com/java/java_user_input.asp> [Accessed 24 May 2026].
