package Server;

import java.io.IOException;

public class StartServer {
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
