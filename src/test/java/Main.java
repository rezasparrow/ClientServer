import client.RunClient;
import server.RunServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dotin School1 on 4/13/2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        new Thread(new RunServer()).start();

        Thread.sleep(1000);
        List<Thread> threads = new ArrayList<>();
        for(int i = 0 ; i < 3 ; ++i){
            threads.add(new Thread(new RunClient(String.format("terminal%s.xml" , i+1))));
        }

        for(Thread thread : threads){
            thread.start();
        }

        for(Thread thread : threads){
            thread.join();
        }


    }
}
