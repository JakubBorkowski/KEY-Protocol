import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 2011);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        Scanner read = new Scanner(System.in);
        String clientmsg, servermsg; //wiadomość od klienta do serwera, wiadomość od serwera do klienta
        //clientmsg = "key_in-get{test};";
        while (true) {
            clientmsg = read.nextLine();
            out.writeUTF(clientmsg);
            if (clientmsg.equals("exit"))
                break;
            servermsg = in.readUTF();
            System.out.println("Server: " + servermsg);
        }
        in.close();
        out.close();
        socket.close();
    }
}