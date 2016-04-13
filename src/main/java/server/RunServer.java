package server;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunServer {
    public static void main(String[] args) throws IOException, ParseException {
        ServerFileManager serverFileManager = new ServerFileManager();
        List<Deposit> deposits = serverFileManager.getDeposits();
        ServerSocket serverSocket = new ServerSocket(serverFileManager.getPort());
        System.out.println("Server listen on port :" + serverSocket.getLocalPort());
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        while (true) {
            threadPool.submit(new MultiThreadServer(serverSocket, deposits, serverFileManager));
        }
    }
}
