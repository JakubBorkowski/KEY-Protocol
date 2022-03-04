import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static void closeConnection(DataInputStream in, DataOutputStream out, Socket socket) throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Waiting for connection...");
        Socket socket = null;
        boolean connected = false;
        while(!connected){
            try {
                socket = new Socket("localhost", 2011);
                connected = true;
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        System.out.println("Connected to server.");
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        Scanner read = new Scanner(System.in);
        String clientmsg, servermsg = ""; //wiadomość od klienta do serwera, wiadomość od serwera do klienta
        while (true) {
            clientmsg = read.nextLine();
            try {
                out.writeUTF(clientmsg);
            } catch (IOException e) {
                System.out.println("Connection lost!");
                closeConnection(in, out, socket);
                System.exit(1);
            }
            if (clientmsg.equals("exit"))
                break;
            try {
                servermsg = in.readUTF();
            } catch (IOException e) {
                System.out.println("Connection lost!");
                closeConnection(in, out, socket);
                System.exit(1);
            }
            System.out.println("Server: " + servermsg);
        }
        closeConnection(in, out, socket);
    }
}