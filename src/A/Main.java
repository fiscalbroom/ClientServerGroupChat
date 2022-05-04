package A;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Sintassi: java ChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        ChatServer server = new ChatServer(port);
        server.execute();
    }
}
