package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

    private final ArrayList<String> keys = new ArrayList<>(); //baza kluczy
    private final String password = "haslo"; //hasło może zawierać znaki, cyfry i podkreślenie

    private String processMessage(String clientmsg){
        String servermsg;
        Pattern pattern = Pattern.compile("key_in-(get|set)\\{(\\w*):?(\\w*)};");
        Matcher matcher = pattern.matcher(clientmsg);
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
        return servermsg;
    }

    private void closeConnection(DataInputStream in, DataOutputStream out,
                                 Socket socket, ServerSocket serverSocket) throws IOException {
        in.close();
        out.close();
        socket.close();
        serverSocket.close();
    }

    public void start() throws IOException {
        keys.add("test"); //testowy klucz
        String clientmsg = "", servermsg; //wiadomość od klienta do serwera, wiadomość od serwera do klienta
        ServerSocket ss = new ServerSocket(2011);
        Socket cs = ss.accept();
        System.out.println("Waiting for connection...");
        if (cs.isConnected())
            System.out.println("Client connected.");
        DataInputStream in = new DataInputStream(cs.getInputStream());
        DataOutputStream out = new DataOutputStream(cs.getOutputStream());
        while (true) {
            try {
                clientmsg = in.readUTF();
            } catch (IOException e) {
                System.out.println("Connection lost!");
                closeConnection(in, out, cs, ss);
                System.exit(1);
            }
            System.out.println("Client: " + clientmsg);
            if (clientmsg.equals("exit"))
                break;
            servermsg = processMessage(clientmsg);
            System.out.println("Sending response: " + servermsg);
            try {
                out.writeUTF(servermsg);
            } catch (IOException e) {
                System.out.println("Connection lost!");
                closeConnection(in, out, cs, ss);
                System.exit(1);
            }
        }
        closeConnection(in, out, cs, ss);
    }
}