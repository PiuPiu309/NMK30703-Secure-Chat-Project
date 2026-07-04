package chatclient;

import java.io.*;
import java.net.*;
import java.util.Scanner; // Fixes "cannot find symbol: scanner"
import java.io.PrintWriter; // Fixes "cannot find symbol: writer"

public class ChatClient {

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 1234;

        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("--- Connected to Secure Server ---");

            // 1. Thread to listen for messages FROM the server (Background)
            new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String serverMessage;
                    while ((serverMessage = reader.readLine()) != null) {
                        // DECRYPT when message arrives
                        String decryptedMsg = EncryptionUtil.decrypt(serverMessage);
                        System.out.println("\n[Broadcast]: " + decryptedMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Connection to server lost.");
                }
            }).start();

            // 2. Initialize the writer and scanner (This fixes your error!)
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter your username to start:");
            String username = scanner.nextLine();
            writer.println(EncryptionUtil.encrypt(username + " has joined the chat."));

            // 3. Main loop to send messages
            while (true) {
                String text = scanner.nextLine();

                // ENCRYPT before sending
                String encryptedMsg = EncryptionUtil.encrypt(username + ": " + text);

                writer.println(encryptedMsg);
                System.out.println("[Sent Encrypted]: " + encryptedMsg);
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
