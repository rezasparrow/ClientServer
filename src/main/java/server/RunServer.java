package server;

;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Dotin School1 on 4/9/2016.
 */
public class RunServer {
    public static void main(String[] args) throws IOException, ParseException {
        ServerFileManager serverFileManager = new ServerFileManager();
        List<Deposit> deposits = serverFileManager.getDeposits();
        ServerSocket serverSocket = new ServerSocket(serverFileManager.getPort());
//        serverSocket.setSoTimeout(10000);
        System.out.println("Server listen on port :" + serverSocket.getLocalPort());
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        while (true) {
            Socket sock = serverSocket.accept();
            System.out.println("Connected");
            threadPool.submit(new MultiThreadServer(sock , deposits , serverFileManager));
        }
    }
}
