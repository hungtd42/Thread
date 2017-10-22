
package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class client {
    JFrame clientFrame;
    JPanel clientPanel;
    JTextArea textArea_mess;
    JTextField textField_mess;
    JTextField textField_user;
    JButton button_send;
    JScrollPane scrollPane_mess;
    //
    Socket s;
    PrintWriter write;
    BufferedReader read;
    
    Random r = new Random();
    String b[]={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    public  String[] randomA = new String[3];
    int x =99;
    
    
    
    
    public static void main(String []args){
        client c = new client();
        c.createForm();
    }
    
    public void createForm(){
        clientFrame = new JFrame("Client");
        clientFrame.setSize(800, 600); 
        
        clientPanel = new JPanel();
        
        textArea_mess = new JTextArea();
        textArea_mess.setEditable(false);
        
        textField_mess = new JTextField(38);
        textField_mess.addKeyListener(new SendEnterListenner()); 
        
        button_send = new JButton("Send");
        button_send.addActionListener(new SendButtonEnter());
        
        textField_user = new JTextField(10);
        int[]ran = new int [9];
        for(int i=0;i<ran.length;i++){
            ran[i]= r.nextInt(x);
            textField_user.setText("Client "+ran[i]);
        } 
        
        scrollPane_mess = new JScrollPane(textArea_mess);
        scrollPane_mess.setPreferredSize(new Dimension(700, 500));
        scrollPane_mess.setMinimumSize(new Dimension(700, 500));
        scrollPane_mess.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_mess.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        if (!connectToServer()) { 
            
        }
        Thread t = new Thread(new MessServerListener());
        t.start();
        clientPanel.add(scrollPane_mess);
        clientPanel.add(textField_user);
        clientPanel.add(textField_mess);
        clientPanel.add(button_send);
        
        clientFrame.getContentPane().add(BorderLayout.CENTER,clientPanel);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setVisible(true); 
    }
    public boolean connectToServer(){
        try{
            s= new Socket("192.168.0.102",5595);
            read = new BufferedReader(new InputStreamReader(s.getInputStream()));
            write = new PrintWriter(s.getOutputStream());
            ShowTextMess("Netword conn !!!"+s.getInetAddress());
            
            return true;
        }catch(IOException e){
            ShowTextMess("Unders");
            
            e.printStackTrace();
            
            return false;
        }
    }//
    
    public void SendMessToSv(){
        write.println(textField_user.getText() + ":" + textField_mess.getText());
        write.flush();
        
        textField_mess.setText("");
        textField_mess.requestFocus();
    }
    
    public void ShowTextMess(String mess){
        textArea_mess.append(mess+"\n"); 
    }//
    
    public class SendEnterListenner implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode()== KeyEvent.VK_ENTER) {
                SendMessToSv();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}
    }//
    public class SendButtonEnter implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            SendMessToSv();
        }
    }
    
    public class MessServerListener implements Runnable{

        
        @Override
        public void run() {
            String str;
            try {
                while((str=read.readLine())!=null){
                    ShowTextMess(str);
                    textArea_mess.setCaretPosition(textArea_mess.getText().length()); 
                }
            } catch (IOException ex) {
                ShowTextMess("Und");
                ex.printStackTrace();
            }
        }
    }
}
