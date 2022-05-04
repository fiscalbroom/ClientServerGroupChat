package A;

import java.io.*;
import java.net.*;
import java.util.*;


public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "Nuovo utente connesso: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);

            } while (!clientMessage.equals("Arrivederci"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " ha abbandonato la chat.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Errore in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Utenti connessi: " + server.getUserNames());
        } else {
            writer.println("Non ci sono altri utenti connessi");
        }
    }


    void sendMessage(String message) {
        writer.println(message);
    }
}
