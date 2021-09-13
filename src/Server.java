import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

    public static void main(String[] args) throws Exception {
        ArrayList<String> keys = new ArrayList<String>(); //baza kluczy
        keys.add("test"); //testowy klucz
        Pattern pattern = Pattern.compile("key_in-(get|set)\\{(\\w*):?(\\w*)\\};");
        Matcher matcher;
        String clientmsg, servermsg = ""; //wiadomość od klienta do serwera, wiadomość od serwera do klienta
        String password = "haslo"; //hasło może zawierać znaki, cyfry i podkreślenie
        ServerSocket ss = new ServerSocket(2011);
        System.out.println("Waiting for connection...");
        Socket cs = ss.accept();
        if (cs.isConnected())
            System.out.println("Client connected.");
        DataInputStream in = new DataInputStream(cs.getInputStream());
        DataOutputStream out = new DataOutputStream(cs.getOutputStream());

        while (true) {
            clientmsg = in.readUTF();
            System.out.println("Client: " + clientmsg);
            if (clientmsg.equals("exit"))
                break;
            matcher = pattern.matcher(clientmsg);
            if (matcher.find()) {
                servermsg = "key_out-{";
                if (matcher.group(1).equals("get")) {
                    servermsg += matcher.group(2);
                    if (keys.contains(matcher.group(2)))
                        servermsg += ":yes};";
                    else
                        servermsg += ":not};";
                } else if (matcher.group(1).equals("set")) {
                    servermsg += matcher.group(3);
                    if (matcher.group(2).equals(password)) {
                        keys.add(matcher.group(3));
                        servermsg += ":yes};";
                    } else
                        servermsg += ":not};";
            }
            } else {
                servermsg ="key_out-{" + clientmsg + ":error};";
            }
            out.writeUTF(servermsg);
        }
        cs.close();
        in.close();
        out.close();
    }
}