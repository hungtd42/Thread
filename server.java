
package test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    ServerSocket sever;
    ArrayList<PrintWriter> list_write;//create arraylist
    final int lv_er = 1;//check error or successfuly
    final int lv_succ = 0;
    
    public static void main(String []args){
        Server s = new Server();
        if (s.runServer()) {//tao cong port
            s.listenToClient();//chap nhan ket noi port //khi nhan enter write se gui di
        }
        else{}
    }
    public void textConsole(String str,int level){//in ra thong bao
        if (level == lv_er) {
            System.err.println(str+"\n");
        }else{
            System.out.println(str+"\n");
        }
    }

    
    

    public void listenToClient() {
        while(true){
            try{
                Socket socket = sever.accept();
                PrintWriter write = new PrintWriter(socket.getOutputStream());//viet va nhan enter r gui dl 
                list_write.add(write);//luu PrintWrite vào m?ng list_write ki?m tra mà g?i sang client
                
                //System.out.println(list_write);
                Thread thread = new Thread(new ClientThread(socket));
                thread.start();
                
                
            }catch(IOException ex){
                
                ex.printStackTrace();
            }
        }
    }
    public boolean runServer() {
        try {
            sever = new ServerSocket(5595);//tao port ket noi
            textConsole("server ->>",lv_succ);//
            list_write = new ArrayList<PrintWriter>();//
            
            //System.out.println(list_write);
            return true;
        } catch (IOException ex) {
            
            ex.printStackTrace();
            return false;
        }
    }

    public void botSay(String strBot){
        Iterator itbot =list_write.iterator();
        while(itbot.hasNext()){
            PrintWriter writeBot= (PrintWriter) itbot.next();
            writeBot.println(strBot);
            writeBot.flush();
        }
    }
    public class ClientThread implements Runnable {//thread

        Socket socket;
        BufferedReader read;
        public ClientThread(Socket socket) {
            
            try {
                this.socket = socket;
                read = new BufferedReader(new InputStreamReader(socket.getInputStream()));//doc dl
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String string;
            try {
                while((string = read.readLine())!=null){
                    textConsole("Client:\n"+string, lv_succ);
                    sendToAllClient(string); //nhan dl khi nhan button or enter su dung iterator +hasNext ki?m tra và g?i di
                    
                    if (string.contains("hi")) {
                        botSay("Hello..");
                    }else if(string.contains("H")){
                        botSay("Men");
                    }
                }
            } catch (IOException ex) {
                //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void sendToAllClient(String str){
        Iterator it = list_write.iterator();//kiem tra cac ph?n t? ? m?ng aray
        //System.out.println(it);
        while(it.hasNext()){//hasNext ki?m tra ti?p t?c c?a list_write  và d?y dl di
            PrintWriter write = (PrintWriter) it.next();
            write.println(str);
            write.flush();//d?y du li?u di
        }
    }
    
}
