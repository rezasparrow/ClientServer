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
    List<Transaction> transactions;

    public Terminal(String id, String type, ServerInformation serverInformation) {
        this.id = id;
        this.type = type;
        this.serverInformation = serverInformation;

        this.transactions = new ArrayList<Transaction>();

    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }
}
