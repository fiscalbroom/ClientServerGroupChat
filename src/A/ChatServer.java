package A;

import java.io.*;
import java.net.*;
import java.util.*;


public class ChatServer {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server chat aperto sulla porta " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Nuovo utente connesso");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Errore nel server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }


    void addUserName(String userName) {
        userNames.add(userName);
    }


    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("L'utente " + userName + " ha abbandonato la chat");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }


    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
