package network;

/**
 * This class establish the connection to the server and operates the client-side..
 * @author socho
 * @version 1.0
 */
import java.io.*;
import java.net.*;
import messages.Message;

public class Client extends Thread{
    private String username;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = true;

    /**ClientUI is missing */
    public Client(String ip, int port, String username) {
        try {
            this.username = username;
            this.clientSocket = new Socket(ip, port);
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());

            this.out.writeObject(new ConnectMessage(this.username));
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Could not establish connection to " + ip + ":" + port);
        }
    }

    /**checks if the connection is alright.*/
    public boolean isOk(){
        return (clientSocket != null) && (clientSocket.isConnected()) && !(clientSocket.isClosed());
    }

    public void run(){
        /**while(running){
            try{
                Message m = (Message) in.readObject();
            }catch(ClassNotFoundException | IOException e){
                break;
            }
        }*/
    }

    public void sendToServer(Message m) throws IOException{
        this.out.writeObject(m);
        out.flush();
        out.reset();
    }

    /** clients disconnects and closes sockets.*/
    public void disconnect(){
        running = false;
        try{
            if (!clientSocket.isClosed()){
                this.out.writeObject(new DisconnectMessage(this.username));
                clientSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}