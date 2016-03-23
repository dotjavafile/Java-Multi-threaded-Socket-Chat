/*
 */
package com.chatclient;


import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class ChatClientGui extends javax.swing.JFrame {

    //Custom Variable Declarations

    protected String userName;
    protected Socket tempSock;
    protected BufferedReader tempReader;
    protected PrintWriter tempWriter;
    protected ArrayList<String> onlineUsers;
    protected boolean isConnected = false;

    protected final String EOT = "<qwerty>"; //end of transmission;
    protected final String TOKEN = "<spqr>";
    protected static final String TYPE_CONNECT = "connect";
    protected static final String TYPE_DISCONNECT = "disconnect";
    protected static final String TYPE_CHAT = "chat";
    protected static final String TYPE_USERNAME = "username";

    //End of Custome Variable Declarations
    //Html
    String head_part = "<html>\n"
            + " <head>\n"
            + "\n"
            + " </head>\n"
            + " <body>\n"
            + "<div>\n";

    String tail_part = "</div>\n"
            + " </body>\n"
            + "</html>";
    String body_part = "";
    String full_part = head_part + body_part + tail_part;
    //

    /**
     * Creates new form ChatClientGui
     */
    public ChatClientGui() {
        initComponents();
    }

    public class IncomingReader implements Runnable {

        Socket socket;
        PrintWriter writer;
        BufferedReader bufferedReader;
        StringBuffer tempBuffer;
        String finalstring;

        public IncomingReader(Socket socket, PrintWriter writer) {
            this.socket = socket;
            this.writer = writer;
            tempBuffer = new StringBuffer("");
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException ex) {
                displayAppendln("<span style=\"color:red;\">Buffered reader error</span>");
            }
        }

        @Override
        public void run() {
            String message;
            while (isConnected) {
                tempBuffer.delete(0, tempBuffer.length());
                try {
                    while ((message = bufferedReader.readLine()) != null) {
                        tempBuffer.append(message + "\n");
                        Pattern pattern = Pattern.compile(EOT);
                        Matcher matcher = pattern.matcher(tempBuffer.toString());
                        System.out.println(tempBuffer.toString());
                        if (matcher.find()) {
                            tempBuffer.delete(tempBuffer.length() - EOT.length() - 1 , tempBuffer.length());
                            System.out.println("after trim: " + tempBuffer.toString());
                            break;
                        }
                    }
                    System.out.println("out of loop");
                    System.out.println(tempBuffer.toString());
                    String[] data;
                    data = tempBuffer.toString().split(TOKEN);

                    for (int i = 0; i < data.length; i++) {
                        System.out.println(data[i] + " *");
                    }

                    System.out.println(data[0] + " " + data[1] + " " + data[2]);
                    switch (data[2]) {  //Data[2] is the last valid position in a size 3 array
                        case TYPE_CONNECT:
                            onlineUsers.add(data[0]);
                            displayAppendln("<span style=\"color:blue;\"><b>" + data[0] + " just joined the server.</span>");
                            break;
                        case TYPE_DISCONNECT:
                            onlineUsers.remove(data[0]);
                            displayAppendln("<span style=\"color:blue;\"><b>" + data[0] + " just left the server.</span>");
                            break;
                        case TYPE_CHAT:
                            displayAppendln("<span style=\"color:blue;\"><b>" + data[0] + " : </b></span>" + data[1]);
                            break;
                        case TYPE_USERNAME:
                            onlineUsers.add(data[0]);
                            break;
                        default:

                    }

                } catch (IOException ex) {
                    displayAppendln("Buffered reader error from server");
                    isConnected = false;
                    ex.printStackTrace();
                }

            }
            displayAppendln("exiting");
        }
    }

    public class ShowUserList implements Runnable {

        @Override
        public void run() {
            while (isConnected) {
                updateUserList();
            }
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        saveData = new javax.swing.JFileChooser();
        userNameLabel = new javax.swing.JLabel();
        bannerLabel = new javax.swing.JLabel();
        userNameField = new javax.swing.JTextField();
        connect_btn = new javax.swing.JButton();
        disconnect_btn = new javax.swing.JButton();
        onlineUsersLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        onlineUsersArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        sendTextArea = new javax.swing.JTextArea();
        send_btn = new javax.swing.JButton();
        serverAddressField = new javax.swing.JTextField();
        serverAddressLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        displayTextArea = new javax.swing.JEditorPane();
        saveLog_btn = new javax.swing.JButton();

        saveData.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        saveData.setApproveButtonText("Save");
        saveData.setMaximumSize(new java.awt.Dimension(500, 400));
        saveData.setMinimumSize(new java.awt.Dimension(500, 400));
        saveData.setPreferredSize(new java.awt.Dimension(500, 400));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat Client 2.0 ");
        setResizable(false);

        userNameLabel.setText("User Name:");

        bannerLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/chatclient/sinewave.gif"))); // NOI18N

        connect_btn.setText("Connect");
        connect_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connect_btnActionPerformed(evt);
            }
        });

        disconnect_btn.setText("Disconnect");
        disconnect_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnect_btnActionPerformed(evt);
            }
        });

        onlineUsersLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        onlineUsersLabel.setText("Online Users");

        onlineUsersArea.setEditable(false);
        onlineUsersArea.setBackground(new java.awt.Color(0, 0, 0));
        onlineUsersArea.setColumns(20);
        onlineUsersArea.setForeground(new java.awt.Color(255, 255, 255));
        onlineUsersArea.setRows(5);
        onlineUsersArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(onlineUsersArea);

        sendTextArea.setColumns(20);
        sendTextArea.setRows(5);
        jScrollPane2.setViewportView(sendTextArea);

        send_btn.setText("Send");
        send_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send_btnActionPerformed(evt);
            }
        });

        serverAddressField.setText("127.0.0.1");
        serverAddressField.setEnabled(false);

        serverAddressLabel.setText("Server:");

        displayTextArea.setEditable(false);
        displayTextArea.setContentType("text/html"); // NOI18N
        displayTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane4.setViewportView(displayTextArea);

        saveLog_btn.setText("Save Log");
        saveLog_btn.setEnabled(false);
        saveLog_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveLog_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bannerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveLog_btn))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(send_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 21, Short.MAX_VALUE)
                                .addComponent(userNameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(serverAddressLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(serverAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(connect_btn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(disconnect_btn))
                            .addComponent(jScrollPane4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(onlineUsersLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bannerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveLog_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(connect_btn)
                                .addComponent(disconnect_btn))
                            .addComponent(serverAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(serverAddressLabel)
                            .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(userNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(send_btn, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(onlineUsersLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ListenThread() {

    }


    private void connect_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connect_btnActionPerformed
        // TODO add your handling code here:
        if (!isConnected) {
            if (userNameField.getText().equals("") || serverAddressField.getText().equals("")) {
                JOptionPane.showMessageDialog(rootPane, "Can not leave a field empty", "Field empty error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            onlineUsers = new ArrayList<String>();
            userName = userNameField.getText();
            userNameField.setEditable(false);
            serverAddressField.setEditable(false);
            
            

            try {
                
                //tempSock = new Socket(serverAddressField.getText(), 10101);
                tempSock = new Socket("mountainview.sytes.net", 10101);

                
                InputStreamReader streamreader = new InputStreamReader(tempSock.getInputStream());
                tempReader = new BufferedReader(streamreader);
                tempWriter = new PrintWriter(tempSock.getOutputStream());
                displayAppendln("<span style=\"color:green\">Connected to the server.</span>");
                tempWriter.println(clientProtocolText("has connected.", TYPE_CONNECT));
                tempWriter.flush();
                isConnected = true;
                (new Thread(new IncomingReader(tempSock, tempWriter))).start();
                (new Thread(new ShowUserList())).start();
            } catch (Exception ex) {
                displayAppendln("<span style=\"color:red\">Could not connect to the specified server.</span>");
                userNameField.setEditable(true);
                serverAddressField.setEditable(true);
                ex.printStackTrace();
            }

        } else {
            displayAppendln("<span style=\"color:red\">You are already connected.</span>");
        }
    }//GEN-LAST:event_connect_btnActionPerformed

    private void disconnect_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnect_btnActionPerformed
        if (isConnected) {
            try {
                // TODO add your handling code here:
                isConnected = false;
                tempWriter.println(userName+TOKEN + TOKEN + TYPE_DISCONNECT +EOT);
                tempWriter.close();
                tempSock.close();
                onlineUsers.clear();
                updateUserList();
                displayAppendln("<span style=\"color:red\">Connection closed.</span>");
                userNameField.setEditable(true);
                serverAddressField.setEditable(true);
            } catch (IOException ex) {

            }
        } else {
            displayAppendln("<span style=\"color:red\">You are not connected to anyting.</span>");
        }
    }//GEN-LAST:event_disconnect_btnActionPerformed

    private void send_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_send_btnActionPerformed
        // TODO add your handling code here:
        if (!sendTextArea.getText().equals("")) {
            try {
                tempWriter.println(clientProtocolText(sendTextArea.getText(), TYPE_CHAT));
                tempWriter.flush();
            } catch (Exception ex) {
                displayAppendln("<span style=\"color:red\">Message not sent. Probably because server is down or not conneted to the server.</span>");
                if (isConnected) {
                    isConnected = false;
                }
            }
            sendTextArea.setText("");
        }
    }//GEN-LAST:event_send_btnActionPerformed

    private void saveLog_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveLog_btnActionPerformed
        // TODO add your handling code here:
        int returnVal = saveData.showSaveDialog(jScrollPane4);

        if (returnVal == saveData.APPROVE_OPTION){
            File file = saveData.getSelectedFile();
            System.out.println(file.getName());
            FileWriter writer = null;
            try{
                writer= new FileWriter(file);
                writer.write(full_part);
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                if (writer != null)
                try {
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_saveLog_btnActionPerformed
    protected void displayAppendln(String s) {
        body_part += s + "<br>\n";
        full_part = head_part + body_part + tail_part;
        displayTextArea.setText(full_part);
        //userListArea.setText(full_part);
    }

    protected String clientProtocolText(String message, String type) {
        System.out.println(userName + TOKEN + message + TOKEN + type + EOT);
        return userName + TOKEN + message + TOKEN + type + EOT;
    }

    public void updateUserList() {
        onlineUsersArea.setText("");
        for (String user : onlineUsers) {
            onlineUsersArea.append(user + "\n");;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatClientGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatClientGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatClientGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatClientGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatClientGui().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bannerLabel;
    private javax.swing.JButton connect_btn;
    private javax.swing.JButton disconnect_btn;
    private javax.swing.JEditorPane displayTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea onlineUsersArea;
    private javax.swing.JLabel onlineUsersLabel;
    private javax.swing.JFileChooser saveData;
    private javax.swing.JButton saveLog_btn;
    private javax.swing.JTextArea sendTextArea;
    private javax.swing.JButton send_btn;
    private javax.swing.JTextField serverAddressField;
    private javax.swing.JLabel serverAddressLabel;
    private javax.swing.JTextField userNameField;
    private javax.swing.JLabel userNameLabel;
    // End of variables declaration//GEN-END:variables
}
