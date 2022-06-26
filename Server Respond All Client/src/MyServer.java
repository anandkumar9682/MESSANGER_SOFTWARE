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

import java.util.ArrayList;
import java.util.Iterator;

public class MyServer extends JFrame 
{

    JPanel mainPanel;
    JLabel name,status;
    JTextArea chatMessage;
    
    ServerSocket ss;
    Socket s;
    
    DataOutputStream dout;
    DataInputStream din;
    
    ArrayList al=new ArrayList();

    public static void main(String args[])
    {
        new MyServer();
    }

    public MyServer()
    {

        mainPanel=new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.yellow);
        
        Font font=new Font("Arial",Font.BOLD,12);
        
        name=new JLabel("Name");
        name.setFont(font);
        name.setBounds(25,25,250,20);
        mainPanel.add(name);
        
        chatMessage=new JTextArea();
        chatMessage.setFont(font);
        chatMessage.setForeground(Color.red);
        chatMessage.setCaretPosition(chatMessage.getDocument().getLength());
        chatMessage.setLineWrap(true);
        chatMessage.setEditable(false);
        
        JScrollPane scrollPane=new JScrollPane(chatMessage);
        scrollPane.setBounds(25,50,330,445);
        mainPanel.add(scrollPane);
        
        status=new JLabel("status");
        status.setFont(font);
        status.setBounds(25,500,250,25);
        mainPanel.add(status);

        JLabel createrNane=new JLabel("Created By- Anand Bind");
        createrNane.setBounds(100,530,200,20);
        createrNane.setFont(font);
        createrNane.setForeground(Color.red);
        mainPanel.add(createrNane);

        this.add(mainPanel);

        this.setVisible(true);
        this.setSize(400,600);
        this.setTitle("My Server");
        this.setLocation(0,0);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.connection();
    }

    public void connection()
    {
        try
        {
            String ip= InetAddress.getLocalHost().getHostAddress();
            name.setText("IP :"+ ip+"  Port No: 10");
            chatMessage.setText("Server Start"+"\n");
            status.setText("Status : Client Not Connect");
            ss=new ServerSocket(10);
            while(true)
            {
                s=ss.accept();
                status.setText("Status : Client Connected");
                al.add(s);
                Runnable r=new MyThread(s,al);
                Thread t=new Thread(r);
                t.start();
            }     
        }
        catch(Exception e){} 
                
    }

class MyThread implements Runnable
{
    Socket s;
    ArrayList al;
    public MyThread(Socket s,ArrayList al)
    {
        this.s=s;
        this.al=al;
    }
    public void run()
    {
        String s1;
        try
        {
            DataInputStream din=new DataInputStream(s.getInputStream());
            do
            {
                s1=din.readUTF();
                chatMessage.append(s1+" "+ s +" Client "+ "\n" );
                if(!s1.equals("bye"))
                    tellEveryOne(s1);
                else
                {
                    DataOutputStream dout=new DataOutputStream(s.getOutputStream());
                    dout.writeUTF(s1);
                    dout.flush();
                    al.remove(s);
                }
            }
            while(!s1.equals("bye"));  
        }
        catch(Exception e1){}
    }

    public void tellEveryOne(String s1)
    {
        Iterator i=al.iterator();
        while(i.hasNext())
        {
            try
            {
                Socket sc=(Socket)i.next();
                DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
                dout.writeUTF(s1+" "+ s +" Client");
                dout.flush();
            }
            catch(Exception e){}  
        }
    }
}
    




}




			
			