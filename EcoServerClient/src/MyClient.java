import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import javax.swing.JTextArea;
import java.awt.ComponentOrientation;
import javax.swing.JScrollPane;

import java.net.Socket;
import java.net.UnknownHostException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MyClient extends JFrame implements ActionListener {

Font font;
JPanel signPanel,mainPanel;
JTextField ipType, portType, chatType;
JButton submit, sendMessage,closeb;

String ipAddress;
int portNumber;

JTextArea chatMessage;

DataOutputStream dout;
DataInputStream din;

public void signIn(){

this.setSize(300,300);

font=new Font("Arial",Font.BOLD, 15);

signPanel=new JPanel();
signPanel.setLayout(null);
signPanel.setBackground(Color.pink);
this.add(signPanel);

JLabel ipL=new JLabel("Enter IP Address");
ipL.setBounds(50,25,200,20);
ipL.setFont(font);
signPanel.add(ipL);

ipType=new JTextField();
ipType.setFont(font);
ipType.setBounds(50,50,200,20);
signPanel.add(ipType);

JLabel portL=new JLabel("Enter Port Number");
portL.setFont(font);
portL.setBounds(50,90,200,20);
signPanel.add(portL);


portType=new JTextField();
portType.setFont(font);
portType.setBounds(50,115,200,20);
signPanel.add(portType);

submit=new JButton("Submit");
submit.setBounds(100,150,90,25);
signPanel.add(submit);
submit.addActionListener(this);

}


public void mainPage(){

this.setSize(400,620);

mainPanel=new JPanel();
mainPanel.setLayout(null);
mainPanel.setBackground(Color.green);
this.add(mainPanel);

JLabel name=new JLabel("IP : "+ipAddress);
name.setFont(font);
name.setBounds(25,25,170,20);
mainPanel.add(name);

JLabel port=new JLabel("Port NO : "+portNumber);
port.setFont(font);
port.setBounds(250,25,150,20);
mainPanel.add(port);

chatMessage=new JTextArea();
chatMessage.setFont(font);
chatMessage.setForeground(Color.blue);
chatMessage.setCaretPosition(chatMessage.getDocument().getLength());
chatMessage.setLineWrap(true);
chatMessage.setEditable(false);

JScrollPane scrollPane=new JScrollPane(chatMessage);
scrollPane.setBounds(25,50,330,445);
mainPanel.add(scrollPane);

closeb=new JButton("Close");
closeb.setBounds(25,500,75,25);
closeb.addActionListener(this);
mainPanel.add(closeb);

chatType=new JTextField();
chatType.setFont(font);
chatType.setBounds(105,500,170,25);
mainPanel.add(chatType);

sendMessage=new JButton("Send");
sendMessage.setBounds(280,500,75,25);
sendMessage.addActionListener(this);
mainPanel.add(sendMessage);

JLabel createrNane=new JLabel("Created By- GUI POINT");
createrNane.setBounds(100,545,200,20);
createrNane.setFont(font);
createrNane.setForeground(Color.red);
mainPanel.add(createrNane);


}


public void connction(){
try{
	Socket socket=new Socket(ipAddress,portNumber);
	din=new DataInputStream(socket.getInputStream());
	dout=new DataOutputStream(socket.getOutputStream());

mainPage();

}catch( Exception ex){}


}




public void sendMsg(){

	try{
		chatMessage.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

		String sms=chatType.getText();
		dout.writeUTF(sms);
		chatMessage.append(sms +": Your Message"+"\n");
		chatType.setText(null);


recievedMessage();

	}catch(Exception e){}
}

public void recievedMessage(){
	try{
		chatMessage.append(din.readUTF() + "\n");
	}catch(Exception e){}

}



public void actionPerformed(ActionEvent evt){

if(evt.getSource()==submit){

	if(0==ipType.getText().trim().length()){
		JOptionPane.showMessageDialog(signPanel,"Please Enter IP Address");
	}
	else{ ipAddress=ipType.getText().trim();}

	if(0==portType.getText().trim().length()){
		JOptionPane.showMessageDialog(signPanel,"Please Enter Port Number");}
		else{
		 String stringPort=portType.getText().trim();
		 portNumber=Integer.parseInt(stringPort);

this.remove(signPanel);
connction();


		}
	}


else if(evt.getSource()==sendMessage){


if(0==chatType.getText().trim().length()){
	JOptionPane.showMessageDialog(mainPanel,"Please Type Message");}
	else{ sendMsg();   }
}




if(evt.getSource()==closeb){
	try{
		dout.writeUTF("bye");
		System.exit(0);
	}catch(Exception e){}
}


}




	public static void main(String ar[]){

MyClient myObj=new MyClient();

myObj.signIn();

myObj.setVisible(true);
myObj.setTitle("My Client");
myObj.setLocation(600,30);
myObj.setResizable(false);
myObj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}
}