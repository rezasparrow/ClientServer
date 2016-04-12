package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.TransactionInvalidException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

/**
 * Created by Dotin School1 on 4/10/2016.
 */
public class MultiThreadServer implements Runnable {
    private Socket terminalSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private List<Deposit> depositList;
    private ServerFileManager serverFileManager;

    private String terminalType;
    private String terminalId;


    public MultiThreadServer(Socket socket , List<Deposit> deposits , ServerFileManager serverFileManager) throws IOException, ParseException {
        terminalSocket = socket;
        dataOutputStream = new DataOutputStream(terminalSocket.getOutputStream());
        dataInputStream = new DataInputStream(terminalSocket.getInputStream());
        this.serverFileManager = serverFileManager;

        String terminalInfoMessage = dataInputStream.readUTF();
        setTerminalInfo(terminalInfoMessage);
        this.depositList = deposits;

    }

    private void setTerminalInfo(String message) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(message);
        this.terminalId = jsonObject.get("id").toString();
        this.terminalType = jsonObject.get("type").toString();
    }

    private Deposit findDeposit(String id) throws TransactionInvalidException {
        for(Deposit deposit : depositList){
            if(id.trim().equals(deposit.getId().trim())){
                return deposit;
            }
        }
        throw new TransactionInvalidException("the deposit don't exist");
    }

    private void processRequest(String message)
            throws ParseException, TransactionInvalidException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(message);

        String type = jsonObject.get("type").toString();
        Integer amount = Integer.parseInt(jsonObject.get("amount").toString());
        String depositId = jsonObject.get("deposit").toString();

        Deposit deposit = findDeposit(depositId);
        Method method = Deposit.class.getMethod(type , new Class[]{String.class});
        method.invoke(deposit , amount);
    }


    public void run() {

        System.out.println("Just connected to " + terminalSocket.getRemoteSocketAddress());
        try {
            while (true) {

                String processState = "failed";
                String request = dataInputStream.readUTF();
                serverFileManager.addStartTransactionLog(request , terminalId , terminalType);

                try {
                    processRequest(request);
                    dataOutputStream.writeUTF("transaction done");
                } catch (ParseException e) {
                    dataOutputStream.writeUTF("transaction format is invalid");
                } catch (TransactionInvalidException e) {
                    dataOutputStream.writeUTF(e.getMessage());
                } catch (NoSuchMethodException e) {
                    dataOutputStream.writeUTF("transaction type is invalid");
                } catch (InvocationTargetException e) {
                    dataOutputStream.writeUTF(e.getCause().getMessage());

                } catch (IllegalAccessException e) {
                    dataOutputStream.writeUTF("transaction type is invalid");
                }

            }
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                terminalSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

