package com.chatserver;

/*
 */

import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;

public class ChatServerGui extends javax.swing.JFrame {

    //Custome Variables
    ArrayList<PrintWriter> userWriterList;
    ArrayList<String> userNames;
    boolean isServerActive = false;
    Thread serverThread;
    ServerSocket serverSock;

    protected final String EOT = "<qwerty>"; //end of transmission;
    protected final String TOKEN = "<spqr>";
    protected static final String TYPE_CONNECT = "connect";
    protected static final String TYPE_DISCONNECT = "disconnect";
    protected static final String TYPE_CHAT = "chat";
    protected static final String TYPE_USERNAME = "username";
    //Custome Variables Ends Here

    //Html
    String head_part = "<html>\n"
            + " <head>\n"
            + "\n"
            + " </head>\n"
            + " <body>\n"
            + "<div>\n";

    String tail_part = "</div>\n"
            +" </body>\n"
            + "</html>";
    String body_part = "";
    String full_part = head_part + body_part + tail_part;
    //Html End
    public ChatServerGui() {
        initComponents();
        serverEditoPane.setText(full_part);
                
    }
    //Server Thread Below
    public class ServerStart implements Runnable {

        @Override
        public void run() {
            userWriterList = new ArrayList<PrintWriter>();
            userNames = new ArrayList<String>();
            

            (new Thread(new ShowUserList())).start();
            try {
                serverSock = new ServerSocket(10101);

                while (isServerActive) {
                    Socket tempSock = serverSock.accept();
                    PrintWriter tempWriter = new PrintWriter(tempSock.getOutputStream());
                    
                    (new Thread(new ClientHandler(tempSock , tempWriter))).start();

                }
                displayAppendln("stopping server");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    //Thread for showing user list
    public class ShowUserList implements Runnable {

        @Override
        public void run() {
            while (isServerActive) {
                updateUserList();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public class ClientHandler implements Runnable {

        boolean isClientConnected = false;
        Socket socket;
        PrintWriter writer;
        BufferedReader bufferedReader;
        String thisClientName;
        StringBuffer tempBuffer;
        String finalString;
        int count;
        
        public ClientHandler(Socket socket, PrintWriter writer) {
            this.socket = socket;
            this.writer = writer;
            isClientConnected = true;
            tempBuffer = new StringBuffer("");
            finalString = "";
            count = 0;
            try {
                InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(isReader);
            } catch (Exception e) {
                displayAppendln("Failed to initalize buffered reader");
            }

        }

        @Override
        public void run() {
            String message;
            while (isClientConnected && isServerActive) {
                tempBuffer.delete(0, tempBuffer.length());
                try {
                    while ((message = bufferedReader.readLine()) != null) {
                        tempBuffer.append(message + "\n");
                        Pattern pattern = Pattern.compile(EOT);
                        Matcher matcher = pattern.matcher(tempBuffer.toString());
                        //System.out.println(tempBuffer.toString());
                        if (matcher.find()){
                            finalString = tempBuffer.toString();
                            tempBuffer.delete(tempBuffer.length() - EOT.length()- 1 , tempBuffer.length());
                            //System.out.println("after trim: "+ tempBuffer.toString());
                            break;
                        }
                    }
                        System.out.println("out of loop");
                        System.out.println(tempBuffer.toString());
                        System.out.println(finalString);
                        String[] data;
                        data = tempBuffer.toString().split(TOKEN);
                        
                        for (int i = 0;i<data.length; i++)
                            System.out.println(data[i]+ " *");
                        
                        for (int i = 0; i<userWriterList.size();i++){
                            System.out.println(userWriterList.get(i).toString());
                        }
                        switch (data[2]) {  //Data[2] is the last valid position in a size 3 array
                            case TYPE_CONNECT:
                                displayAppendln("<span style=\"color:green;\"><b>" + data[0] + " has conneted succsesfully to server.</b></span>");
                                thisClientName = data[0];
                                sendUserList(writer);
                                System.out.println("connect "+finalString);
                                sendToAll(finalString);
                                userWriterList.add(writer);
                                userNames.add(data[0]);
                                break;
                            case TYPE_DISCONNECT:
                                displayAppendln("<span style=\"color:green;\"><b>" + thisClientName + " has left the server</b></span>");
                                userWriterList.remove(writer);
                                System.out.println("disconnet "+finalString);
                                sendToAll(finalString);
                                userNames.remove(thisClientName);
                                writer.close();
                                socket.close();
                                isClientConnected = false;
                                return;
                            case TYPE_CHAT:
                                displayAppendln("<span style=\"color:blue;\"><b>" + thisClientName + ":</b></span> " + data[1] + "");
                                System.out.println("chat  "+finalString);
                                sendToAll(finalString);
                                break;
                            default:

                        }
                    
                } catch (IOException ex) {
                    displayAppendln("Buffered reader error from " + socket.getInetAddress());
                    userWriterList.remove(writer);
                    userNames.remove(thisClientName);
                    isClientConnected = false;
                    writer.close();
                    ex.printStackTrace();
                }
                
            }
            displayAppendln("exiting");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        saveData = new javax.swing.JFileChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        userListArea = new javax.swing.JTextArea();
        start_btn = new javax.swing.JButton();
        close_btn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        serverEditoPane = new javax.swing.JEditorPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        saveLog_btn = new javax.swing.JButton();

        saveData.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        saveData.setApproveButtonText("Save");
        saveData.setApproveButtonToolTipText("");
        saveData.setDialogTitle("Save The Log");
        saveData.setPreferredSize(new java.awt.Dimension(500, 400));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat Server 2.0 ");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        userListArea.setEditable(false);
        userListArea.setBackground(new java.awt.Color(0, 0, 0));
        userListArea.setColumns(20);
        userListArea.setForeground(new java.awt.Color(255, 255, 255));
        userListArea.setRows(5);
        userListArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane2.setViewportView(userListArea);

        start_btn.setText("Start");
        start_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                start_btnActionPerformed(evt);
            }
        });

        close_btn.setText("Close");
        close_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close_btnActionPerformed(evt);
            }
        });

        serverEditoPane.setEditable(false);
        serverEditoPane.setContentType("text/html"); // NOI18N
        serverEditoPane.setText("<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      \r<b>hello</b>\n    </p>\r\n  </body>\r\n</html>\r\n"); // NOI18N
        serverEditoPane.setToolTipText("");
        serverEditoPane.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane3.setViewportView(serverEditoPane);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Log");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("User List");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/chatserver/sinewave.gif"))); // NOI18N
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel3.setDoubleBuffered(true);

        saveLog_btn.setBackground(new java.awt.Color(204, 204, 204));
        saveLog_btn.setText("Save Log");
        saveLog_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(start_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(close_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saveLog_btn))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jScrollPane3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 690, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(saveLog_btn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(start_btn)
                            .addComponent(close_btn))
                        .addGap(33, 33, 33))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void start_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_start_btnActionPerformed
        // TODO add your handling code here:
        if (!isServerActive) {
            serverThread = new Thread(new ServerStart());
            isServerActive = true;
            serverThread.start();
            displayAppendln("<span style=\"color:red;\"><b>Server thread activated.</b></span>");

        } else {
            displayAppendln("<span style=\"color:red;\"><b>Server already active.</b></span>");
        }
    }//GEN-LAST:event_start_btnActionPerformed

    private void close_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_close_btnActionPerformed
        // TODO add your handling code here:
        if (isServerActive) {
            isServerActive = false;
            try {
                serverSock.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            displayAppendln("<span style=\"color:red;\"><b>Server thread stopped.</b></span>");
        } else {
            displayAppendln("<span style=\"color:red;\"><b>No server thread active.</b></span>");
        }
    }//GEN-LAST:event_close_btnActionPerformed

    private void saveLog_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveLog_btnActionPerformed
        // TODO add your handling code here:
        int returnVal = saveData.showSaveDialog(jScrollPane2);
        
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

    public void updateUserList() {
        userListArea.setText("");
        for (String user : userNames) {
            userListArea.append(user + "\n");
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(ChatServerGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void displayAppendln(String s) {
        body_part += s +"<br>\n" ;
        full_part = head_part + body_part + tail_part;
        serverEditoPane.setText(full_part);
        //serverEditoPane.setCaretPosition(body_part.length());
        //userListArea.setText(full_part);
    }
    
    protected void sendToAll(String s, String type){
        Iterator<PrintWriter> iterator = userWriterList.iterator();
        while(iterator.hasNext()){
            PrintWriter itwriter = iterator.next();
            itwriter.println(serverProtocolText(s, type));
            itwriter.flush();
        }
    }
    protected void sendToAll(String s){
        Iterator<PrintWriter> iterator = userWriterList.iterator();
        while(iterator.hasNext()){
            PrintWriter itwriter = iterator.next();
            itwriter.println(s);
            itwriter.flush();
        }
    }    
    protected String serverProtocolText(String message,String type){
        return TOKEN + message + TOKEN + type + EOT;
    }

    protected void sendUserList(PrintWriter pwriter){
        Iterator<String> names = userNames.iterator();
        while(names.hasNext()){
            String temp = names.next();
            pwriter.println(temp + TOKEN +""+ TOKEN+ TYPE_USERNAME +EOT);
            pwriter.flush();
            System.out.println("Hello " + temp + TOKEN + "" + TOKEN + TYPE_USERNAME +EOT);
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
            java.util.logging.Logger.getLogger(ChatServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatServerGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton close_btn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JFileChooser saveData;
    private javax.swing.JButton saveLog_btn;
    private javax.swing.JEditorPane serverEditoPane;
    private javax.swing.JButton start_btn;
    private javax.swing.JTextArea userListArea;
    // End of variables declaration//GEN-END:variables
}
