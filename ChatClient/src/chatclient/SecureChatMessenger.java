/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package chatclient;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.Image;

/**
 *^
 * @author twofa
 */
public class SecureChatMessenger extends javax.swing.JFrame {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String username;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SecureChatMessenger.class.getName());

    /**
     * Creates new form SecureChatMessenger
     */
    public SecureChatMessenger() {
        // Networking variables
        initComponents();

        // 1. Force the layers (Chat on top, Image on bottom)
        // PALETTE_LAYER is higher than DEFAULT_LAYER, so it sits on top.
        jLayeredPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.PALETTE_LAYER);
        jLayeredPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        // 2. The Transparency Chain
        jLayeredPane1.setOpaque(false);
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);

        chatArea.setOpaque(false);
        chatArea.setBackground(new java.awt.Color(0, 0, 0, 0));

        // 3. Make text visible! 
        // Use White for the space background.
        chatArea.setForeground(java.awt.Color.WHITE);
        chatArea.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
       try {
        // 1. Get the icon you already added in Design view
        ImageIcon icon = (ImageIcon) sendButton.getIcon();
        if (icon != null) {
            Image img = icon.getImage();
            
            // 2. Scale it to a professional size (e.g., 25x25 or 30x30 pixels)
            // Change the numbers 25, 25 to make it smaller or bigger
            Image newImg = img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
            
            // 3. Set the newly sized icon back to the button
            sendButton.setIcon(new ImageIcon(newImg));
        }
        
        // 4. Remove the "Send" text so ONLY the icon shows
        sendButton.setText("");
        
        // 5. Make the button look "Flat" and modern (Optional)
        sendButton.setContentAreaFilled(false); // Removes the gray button box
        sendButton.setBorderPainted(false);    // Removes the border
        sendButton.setFocusPainted(false);      // Removes the "click box"
        
    } catch (Exception e) {
        System.out.println("Could not resize icon: " + e.getMessage());
    }
    }

    // This method connects to the server and starts listening
    public void startClient() {
        try {
            // Ask for name
            socket = new Socket("localhost", 1234);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String serverResponse = reader.readLine();
            if (serverResponse != null) {
                String status = EncryptionUtil.decrypt(serverResponse);

                if ("AUTH_REQUEST".equals(status)) {
                    // Ask the user for the password
                    String pass = JOptionPane.showInputDialog(this, "Enter Group Chat Password:");
                    // Encrypt and send it back to server6
                    writer.println(EncryptionUtil.encrypt(pass));

                    // Wait for server to say if it's okay
                    String authResult = EncryptionUtil.decrypt(reader.readLine());
                    if (!"AUTH_SUCCESS".equals(authResult)) {
                        JOptionPane.showMessageDialog(this, "Wrong Password! Closing...");
                        System.exit(0);
                        return;
                    }
                }
            }
            username = JOptionPane.showInputDialog(this, "Enter your username:");
            if (username == null || username.trim().isEmpty()) {
                username = "User" + (int) (Math.random() * 1000);
            }

            writer.println(EncryptionUtil.encrypt(username + " joined the chat."));
            // Connect to server (Ensure Server is running first!)

            // Background thread to listen for messages
            new Thread(() -> {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[Network Packet Received]: " + line);
                        String decrypted = EncryptionUtil.decrypt(line);
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
                        String time = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());

                        // Update display to: [14:30] John: Hello!
                        chatArea.append("[" + time + "] " + decrypted + "\n");

                        // AUTO-SCROLL: This keeps the latest message visible at the bottom
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
                    }
                } catch (IOException e) {
                    chatArea.append("Connection to server lost.\n");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not connect to server. Is it running?");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        messageField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();

        jRadioButton1.setText("jRadioButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SecureChat Messenger - [Logged In]");
        setBackground(new java.awt.Color(153, 153, 153));
        setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 153, 0));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Encrypted Chat");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(131, 131, 131)
                .addComponent(jLabel1)
                .addContainerGap(139, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, -1));

        messageField.setForeground(new java.awt.Color(204, 204, 204));
        messageField.setText("Type message here...");
        messageField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                messageFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                messageFieldFocusLost(evt);
            }
        });
        messageField.addActionListener(this::messageFieldActionPerformed);
        getContentPane().add(messageField, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 300, 44));

        sendButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chatclient/send-data.png"))); // NOI18N
        sendButton.addActionListener(this::sendButtonActionPerformed);
        getContentPane().add(sendButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 490, 50, 44));

        jLayeredPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setOpaque(false);

        chatArea.setEditable(false);
        chatArea.setBackground(new java.awt.Color(0, 0, 0));
        chatArea.setColumns(20);
        chatArea.setRows(5);
        chatArea.setOpaque(false);
        jScrollPane1.setViewportView(chatArea);

        jLayeredPane1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 340, 434));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chatclient/background2.jpg"))); // NOI18N
        jLayeredPane1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 340, 434));

        getContentPane().add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 52, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        String text = messageField.getText().trim();

        if (!text.isEmpty() && !text.equals("Type message here...")) {

            String fullMessage = username + ": " + text;

            // 1. Encrypt the message
            String encrypted = EncryptionUtil.encrypt(fullMessage);

            // 2. Send the encrypted message to the server (ONLY ONCE)
            writer.println(encrypted);

            // 3. LOG to console for the professor to see
            System.out.println("[Network Packet Sent]: " + encrypted);

            // 4. Clear the field and put the hint back
            messageField.setText("");
            messageFieldFocusLost(null);
        }
    }//GEN-LAST:event_sendButtonActionPerformed

    private void messageFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_messageFieldFocusGained
        if (messageField.getText().equals("Type message here...")) {
            messageField.setText(""); // Clear the box
            messageField.setForeground(java.awt.Color.BLACK); // Change text color to black for typing
        }
// TODO add your handling code here:
    }//GEN-LAST:event_messageFieldFocusGained

    private void messageFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_messageFieldFocusLost
        if (messageField.getText().isEmpty()) {
            messageField.setForeground(java.awt.Color.GRAY); // Make it gray again
            messageField.setText("Type message here..."); // Put the hint back
        }
// TODO add your handling code here:
    }//GEN-LAST:event_messageFieldFocusLost

    private void messageFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageFieldActionPerformed

        sendButtonActionPerformed(evt);
    }//GEN-LAST:event_messageFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            SecureChatMessenger scm = new SecureChatMessenger();
            scm.setVisible(true);
            scm.startClient(); // Start connection after window appears
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField messageField;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
