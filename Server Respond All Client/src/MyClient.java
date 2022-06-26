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
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyClient extends JFrame implements ActionListener {

Font font=new Font("Arial",Font.BOLD, 12);
JPanel signPanel,mainPanel;
JTextField ipType, portType, chatType;
JButton submit, sendMessage,closeb;

String ipAddress;
int portNumber;

JTextArea chatMessage;

DataOutputStream dout;
DataInputStream din;

Socket s;

    public static void main(String ar[])
	{
        new MyClient();
    }

    public MyClient()
    {
        this.setSize(300,300);
        
        signPanel=new JPanel();
        signPanel.setLayout(null);
        signPanel.setBackground(Color.pink);
        
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

        this.add(signPanel);
        
        this.setVisible(true);
        this.setTitle("My Client");
        this.setLocation(600,30);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void mainPage()
    {
        this.setSize(400,620);
        mainPanel=new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.green);
        
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
        //chatMessage.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    
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
        
        JLabel createrNane=new JLabel("Created By- Anand Bind");
        createrNane.setBounds(100,545,200,20);
        createrNane.setFont(font);
        createrNane.setForeground(Color.red);
        mainPanel.add(createrNane);

        this.add(mainPanel);
        this.setVisible(true);

        My m=new My(din , chatMessage);
		Thread t1=new Thread(m);
		t1.start();

    }

    public void actionPerformed(ActionEvent evt)
    {
        if(evt.getSource()==submit)
        {
        	
    	    if(0==ipType.getText().trim().length() || 0==portType.getText().trim().length())
    	    {
    		JOptionPane.showMessageDialog(this,"Please Enter IP Address And Port Number");
    	    }
    	    else
    	    { 
                ipAddress=ipType.getText().trim();
                portNumber= Integer.parseInt(portType.getText().trim());

    	    	this.remove(signPanel);
    	    	this.setVisible(false);
    			try
                {
                	s=new Socket(ipAddress,portNumber);
                	din=new DataInputStream(s.getInputStream());
                	dout=new DataOutputStream(s.getOutputStream());
                	mainPage();
                }
                catch( Exception ex)
                {}
    		}
    	}
    
        else if(evt.getSource()==sendMessage)
        {
            if(0==chatType.getText().trim().length())
            {
            	JOptionPane.showMessageDialog(mainPanel,"Please Type Message");
            }
            else
            { 
                try{
    	        	
    	        	String sms=chatType.getText().trim();
    	        	dout.writeUTF(sms);
    	        	dout.flush();
    	        	//chatMessage.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    	        	//chatMessage.append(sms +": Your Message"+"\n");
    	        	chatType.setText("");
    	        }catch(Exception e){}   
            }
        }
    
        else if(evt.getSource()==closeb)
        {
        	try
        	{
        		dout.writeUTF("stop");
        		System.exit(0);
        	}
        	catch(Exception e)
        	{}
        }
    }
}


class My implements Runnable
{
	JTextArea chatMessage;
	DataInputStream din;
	My(DataInputStream din , JTextArea chatMessage)
	{
	    this.din=din;
	    this.chatMessage=chatMessage;
	}
	public void run()
	{
		String s2="";
		while(true)
		{   
			try
			{
			s2=din.readUTF() ;
			//chatMessage.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			chatMessage.append(s2 + "\n");
		    }
		    catch(Exception e){}  
	    }
    }
}
