# NMK30703-Secure-Chat-Project
A Java-based multithreaded chat application with AES-128 encryption and authentication for NMK30703 Programming for Networking.

🚀 How to Run the Project (For Report & Demo)

To start the application, you only need to execute two specific files in this exact order:

FIRST: Run the Server
File: ChatServer.java (Inside the ChatServer project)
How: Right-click the file and select Run File.
Result: You will see "Server is running..." in the NetBeans output. Keep this running!

SECOND: Run the Client (The GUI)
File: SecureChatMessenger.java (Inside the ChatClient project)
How: Right-click the file and select Run File.

Authentication:
Step 1: It will ask for a password. Type: admin123
Step 2: It will ask for your Username. Type your name.

Note: You can run this file multiple times to have two or three different people chatting at once!

Technical Info for the Report (Team Tip)

Main Files: 
Only ChatServer.java and SecureChatMessenger.java have a main method (the green play button).

Helper File: 
EncryptionUtil.java is a Module. It handles the AES encryption logic. You don't run it; the other files call it automatically.

Architecture:
It is a Client-Server model. If the Server is not running, the Client will show an error.
