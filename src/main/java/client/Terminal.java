package client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dotin School1 on 4/10/2016.
 */
public class Terminal {
    String id;
    String type;
    ServerInformation serverInformation;
    String outLogPath;
    List<Transaction> transactions;

    public Terminal(String id, String type, ServerInformation serverInformation, String outLogPath) {
        this.id = id;
        this.type = type;
        this.serverInformation = serverInformation;
        this.outLogPath = outLogPath;

        this.transactions = new ArrayList<Transaction>();

    }


    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }
}
