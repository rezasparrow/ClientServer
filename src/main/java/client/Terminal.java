package client;

import java.util.ArrayList;
import java.util.List;
import java.net.*;
import java.io.*;

/**
 * Created by Dotin School1 on 4/10/2016.
 */
public class Terminal {
    private String id;
    private String type;
    private ServerInformation serverInformation;
    private List<Transaction> transactions;

    private Socket  client;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

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

    public void connectToServer() throws IOException {
        client = new Socket(serverInformation.getIp() , serverInformation.getPort());

        OutputStream outputStream = client.getOutputStream();
        dataOutputStream = new DataOutputStream(outputStream);

        InputStream inputStream =  client.getInputStream();
        dataInputStream = new DataInputStream(inputStream);
    }

    public String sendData(String Data) throws IOException {
        dataOutputStream.writeUTF(Data);
        return dataInputStream.readUTF();
    }

    public void closeConnection() throws IOException {
        client.close();
    }
}
