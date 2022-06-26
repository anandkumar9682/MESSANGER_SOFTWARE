import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Font;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MyServer extends JFrame {

JPanel mainPanel;
JLabel name,status;
JTextArea chatMessage;

ServerSocket ss;
Socket s;

DataOutputStream dout;
DataInputStream din;

int serverReconnect=0;

MyServer(){

mainPanel=new JPanel();
mainPanel.setLayout(null);
mainPanel.setBackground(Color.yellow);
this.add(mainPanel);

Font font=new Font("Arial",Font.BOLD,15);

name=new JLabel("Name");
name.setFont(font);
name.setBounds(25,25,250,20);
mainPanel.add(name);

chatMessage=new JTextArea();
chatMessage.setFont(font);
chatMessage.setForeground(Color.red);
chatMessage.setCaretPosition(chatMessage.getDocument().getLength());
//chatMessage.setLineWrap(true);
chatMessage.setEditable(false);

JScrollPane scrollPane=new JScrollPane(chatMessage);
scrollPane.setBounds(25,50,330,445);
mainPanel.add(scrollPane);

status=new JLabel("status");
status.setFont(font);
status.setBounds(25,500,250,25);
mainPanel.add(status);

}

public void connection(){

try{ 
String ip= InetAddress.getLocalHost().getHostAddress();
name.setText("IP :"+ ip+"  Port No: 10");
chatMessage.setText("Server Start"+"\n");
status.setText("Status : Client Not Connect");
ss=new ServerSocket(10);
s=ss.accept();
serverReconnect=1;
status.setText("Status : Client Connected");
din=new DataInputStream(s.getInputStream());
dout=new DataOutputStream(s.getOutputStream());
serverChat();
}catch(Exception e){}

}

public void serverChat(){
String str;
try{
do{
str=din.readUTF();
chatMessage.append("Client Message : "+str+ "\n" );
chatMessage.append("Server Message Hello :"+str+"\n");
dout.writeUTF("Server Message Hello : "+str);
dout.flush();

if(str.equals("bye")){
status.setText("Status : Client Disconnected");
 serverReconnect=0;
 s.close();
 ss.close();
 }
} while(serverReconnect==1);
}catch(Exception e){}

connection();

}

public static void main(String args[]){

MyServer myRefObj=new MyServer();
myRefObj.setVisible(true);
myRefObj.setSize(400,600);
myRefObj.setTitle("My Server");
myRefObj.setLocation(0,0);
myRefObj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
myRefObj.setResizable(false);

myRefObj.connection();

}
}




			
			