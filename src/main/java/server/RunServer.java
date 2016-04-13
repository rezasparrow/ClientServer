package server;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunServer implements Runnable {
    public static void main(String[] args) throws IOException, ParseException {
        ServerFileManager serverFileManager = new ServerFileManager();
        final List<Deposit> deposits = serverFileManager.getDeposits();
        ServerSocket serverSocket = new ServerSocket(serverFileManager.getPort());
        System.out.println("Server listen on port :" + serverSocket.getLocalPort());
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        Runnable showBalance = new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for(Deposit deposit : deposits){
                        System.out.println(deposit.getInitBalance());
                    }
                }

            }
        };
        new Thread(showBalance).start();

        while (true) {
            threadPool.submit(new MultiThreadServer(serverSocket, deposits, serverFileManager));
        }
    }

    @Override
    public void run() {
        try {
            main(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
