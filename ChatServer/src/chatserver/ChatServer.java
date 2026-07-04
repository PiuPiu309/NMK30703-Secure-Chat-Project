/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    // List to keep track of all connected clients
    private static final Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        int port = 1234;
        System.out.println("Server is running and waiting for clients...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                // Wait for a new client and start a new thread for them
                new ClientHandler(serverSocket.accept()).start();
                System.out.println("New client connected!");
            }
        } catch (IOException e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }

    // This class handles communication with ONE specific client
    private static class ClientHandler extends Thread {

        private final Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                out = new PrintWriter(socket.getOutputStream(), true);

                // --- STEP 1: AUTHENTICATION (The Security Gate) ---
                // Tell client we need a password
                out.println(EncryptionUtil.encrypt("AUTH_REQUEST"));

                String receivedPassword = reader.readLine();
                if (receivedPassword == null) {
                    return; // 
                }
                String decryptedPassword = EncryptionUtil.decrypt(receivedPassword);

                // Verify password
                if ("admin123".equals(decryptedPassword)) {
                    out.println(EncryptionUtil.encrypt("AUTH_SUCCESS"));
                    System.out.println("Client authenticated successfully.");
                } else {
                    out.println(EncryptionUtil.encrypt("AUTH_FAILED"));
                    System.out.println("Client failed authentication. Connection closed.");
                    socket.close();
                    return; // Stop the thread here
                }

                // --- STEP 2: ACCESS GRANTED (The Chat Room) ---
                // Only add to the list AFTER they pass the password check
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                String serverMessage;
                // This loop keeps the chat going until the client leaves
                while ((serverMessage = reader.readLine()) != null) {
                    String decryptedMsg = EncryptionUtil.decrypt(serverMessage);

                    // Server Logs (Good for your report!)
                    System.out.println("\n[Chat Log]: " + decryptedMsg);
                    System.out.println("[Encrypted Data]: " + serverMessage);

                    // Send to everyone
                    broadcast(serverMessage);
                }

            } catch (IOException e) {
                System.out.println("Client disconnected.");
            } finally {
                // Cleanup when client leaves
                if (out != null) {
                    synchronized (clientWriters) {
                        clientWriters.remove(out);
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        // Send message to all connected clients
        private void broadcast(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }
    }
}
