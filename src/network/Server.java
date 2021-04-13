package network;

/** This class is for setting up the server.
 * @author socho
 * @version 1.0
 */
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import messages.*;
import network.*;

public class Server {
   private ServerSocket hostSocket;
    private boolean running ;

    /** collects all connected clients in a HashMap*/
    private HashMap<String, ServerProtocol> clients = new HashMap<>();

    public synchronized void removeClient(String clientName){
        this.clients.remove(clientName);
    }

    public synchronized boolean userExistsP(String name){
        return this.clients.keySet().contains(name);
    }

    public synchronized void addClient(String name, ServerProtocol protocol){
        this.clients.put(name,protocol);
    }

    public synchronized Set<String> getClientNames(){
        Set<String> clientNames = this.clients.keySet();
        return new HashSet<String>(clientNames);
    }

/** setup server + listen to connection requests from clients*/
    public void listen(){
        running = true;
        try {
            hostSocket = new ServerSocket(ServerSettings.port);
            System.out.println("Server runs") ;
            /** open streams */
            while(running) {
                Socket clientSocket = hostSocket.accept();

            }
        }catch(IOException e){
            if (hostSocket != null && hostSocket.isClosed()){
                System.out.println("Server stopped.");
            }else{
                e.printStackTrace();
            }
        }
    }

    private synchronized void sendTo(List<String> clientNames, Message m){
        List<String> cFails = new ArrayList<String>();
        for(String cName: clientNames){
            try{
                ServerProtocol c = clients.get(cName);
                c.sendToClient((Message)(m.clone()));
            }catch(IOException e){
                cFails.add(cName);
                continue;
            }
        }
        for(String c: cFails){
            System.out.println("Client " + c + " removed (because of send failure).");
            removeClient(c);
        }
    }
    /** send to all clients */
    public void sendToAll(Message m){
    sendTo(new ArrayList<String>(getClientNames(),(Message)(m.clone())));
    }
    /** send to all clients except for one */
    public void sendToAllBut(String name, Message m){
        synchronized (this.clients){
            Set<String> senderList = getClientNames();
            senderList.remove(name);
            sendTo(new ArrayList<String>(senderList),m);
        }
    }

    /** stops the server*/
    public void stopServer(){
        running = false;
        if (!hostSocket.isClosed()){
            try{
                hostSocket.close();
            }catch(IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
    }


}