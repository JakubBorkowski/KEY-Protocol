package Client;

import java.io.IOException;

public class StartClient {
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
