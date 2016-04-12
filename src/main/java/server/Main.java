package server;

;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by Dotin School1 on 4/9/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        ServerFileManager serverFileManager = new ServerFileManager();
        List<Deposit> deposits = serverFileManager.getDeposits();
        ServerSocket serverSocket = new ServerSocket(serverFileManager.getPort());
        serverSocket.setSoTimeout(10000);
        while (true) {
            Socket sock = serverSocket.accept();
            System.out.println("Connected");
            new Thread(new MultiThreadServer(sock , deposits)).start();
        }
    }
}
