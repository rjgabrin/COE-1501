//Ryan Gabrin
//Due: 11/26/2018
//Email: rjg69@pitt.edu
//Peoplesoft: 4101706
//Khattab 1501 Tu/Th 2:30-3:45
//Assignment 4

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.BigInteger;

public class SecureChatClient extends JFrame implements Runnable, ActionListener{

    //set port value
    public static final int PORT = 8765;

    //declare necessary variables
    ObjectInputStream myReader;
    ObjectOutputStream myWriter; 
    
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;

    String myName, serverName;
    Socket connection;

    BigInteger E;
    BigInteger N;
    BigInteger key;

    private byte[] encryptName;
    BigInteger encryptKey;
    SymCipher cipher;
    String cipherType;

    public SecureChatClient(){

        try{

            //retrieve user input for name and server values
            myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
            serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
            InetAddress addr = InetAddress.getByName(serverName);
            connection = new Socket(addr, PORT);   // Connect to server with new
            // Socket

            myWriter = new ObjectOutputStream(connection.getOutputStream());
            myWriter.flush(); 

            myReader = new ObjectInputStream(connection.getInputStream()); 

            E = (BigInteger) myReader.readObject(); 
            N = (BigInteger) myReader.readObject();
            System.out.println("Key E: " + E + "\nKey N: " + N);

            cipherType = (String) myReader.readObject();  
            System.out.println("Encryption type: " + cipherType);

            if (cipherType.equalsIgnoreCase("sub")){

                cipher = new Substitute();
                
            } else if (cipherType.equalsIgnoreCase("add")){

                cipher = new Add128();

            }

            key = new BigInteger(1, cipher.getKey()); 
            System.out.println("Symmetric Key: " + key);
            encryptKey = key.modPow(E, N); 

            myWriter.writeObject(encryptKey);
            myWriter.flush(); 

            encryptName = cipher.encode(myName);

            myWriter.writeObject(encryptName); 
            myWriter.flush();

            
            this.setTitle(myName);

            Box b = Box.createHorizontalBox();
            outputArea = new JTextArea(8, 30);
            outputArea.setEditable(false);
            b.add(new JScrollPane(outputArea));

            outputArea.append("Welcome to the Chat Group, " + myName + "\n");
            //text field to add data
            inputField = new JTextField("");
            inputField.addActionListener(this);

            //generate window to input messages
            prompt = new JLabel("Type your messages below:");
            Container c = getContentPane();

            c.add(b, BorderLayout.NORTH);
            c.add(prompt, BorderLayout.CENTER);
            c.add(inputField, BorderLayout.SOUTH);

            Thread outputThread = new Thread(this);  
            outputThread.start();                    

            addWindowListener(

                    new WindowAdapter(){

                        public void windowClosing(WindowEvent e){

                            try{
                                //alert of the client window closing
                                myWriter.writeObject(cipher.encode("CLIENT CLOSING"));
                                //flush data
                                myWriter.flush();

                            } catch (IOException io){
                                //notify of an error closing the client window
                                System.out.println("Problem closing client!");
                            }
                            //close the program
                            System.exit(0);

                        }

                    }

            );

            setSize(500, 200);
            setVisible(true);

        } catch (Exception e){

            //Notify the user that the client failed to start
            System.out.println("Problem starting client!");

        }

    }

    public void run(){
        //infinite loop
        while (true){
            //try/catch block
            try{
                //set the cencrypted message, decode the message
                byte[] cryptMsg = (byte[]) myReader.readObject();
                String currMsg = cipher.decode(cryptMsg);
                outputArea.append(currMsg + "\n");
                
                byte[] bytes = currMsg.getBytes(); 
                //display desired decryption data
                System.out.println("Recieved array of bytes: " + Arrays.toString(cryptMsg));
                System.out.println("Decrypted array of bytes: " + Arrays.toString(bytes));
                System.out.println("Corresponding string: " + currMsg);

            } catch (Exception e){

                //Notify of an error and close the client window
                System.out.println(e + ", closing client!");
                break;
            }

        }
        //end the program
        System.exit(0);

    }

    public void actionPerformed(ActionEvent e){
        //set the necessary variables
        String currMsg = e.getActionCommand();    
        inputField.setText("");
        //try/catch block
        try{
            //set the message and myWriter
            currMsg = myName + ":" + currMsg;
            byte[] byteMsg = cipher.encode(currMsg);
            myWriter.writeObject(byteMsg);
            //avoid deadlock 
            myWriter.flush();
            byte[] bytes = currMsg.getBytes(); 

            //display desired encryption data
            System.out.println("Original String Message: " + currMsg);
            System.out.println("Array of bytes: " + Arrays.toString(bytes));
            System.out.println("Encrypted array of bytes: " + Arrays.toString(byteMsg));

        } catch (IOException io){

            //Notify that an error occurred when sending the message
            System.err.println("Error: Failed to send message to server!");

        }

    }

    public static void main(String[] args){

        //create the new client and set the defualt close operation
        SecureChatClient JR = new SecureChatClient();
        JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }
}
