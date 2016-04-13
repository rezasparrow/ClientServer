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

        String type = jsonObject.get("type").toString().trim();
        Integer amount = Integer.parseInt(jsonObject.get("amount").toString());
        String depositId = jsonObject.get("deposit").toString();

        Deposit deposit = findDeposit(depositId);
        Method method = Deposit.class.getDeclaredMethod(type , new Class[]{Integer.TYPE});
        method.invoke(deposit , amount);
    }

    private void sendSuccessResponseMessage(String responseMessage) throws IOException {
        dataOutputStream.writeUTF((char)27 + "[32m" +responseMessage  + (char)27 + "[0m");
    }

    private void sendFailedResponseMessage(String responseMessage) throws IOException {
        dataOutputStream.writeUTF((char)27 + "[31m" +responseMessage  + (char)27 + "[0m");
    }

    public void run() {

        System.out.println("Just connected to " + terminalSocket.getRemoteSocketAddress());
        try {
            while (true) {

                String processState = "failed";
                String request = dataInputStream.readUTF();
//                System.out.println(dataInputStream.available());
                serverFileManager.addStartTransactionLog(request , terminalId , terminalType);

                boolean finishSuccess = false;
                try {
                    processRequest(request);
                    serverFileManager.addFinishTransactionSuccessLog(request , terminalId , terminalType);
                    sendSuccessResponseMessage("transaction done");
                    finishSuccess = true;
                } catch (ParseException e) {
                    sendFailedResponseMessage("transaction format is invalid");
                } catch (TransactionInvalidException e) {
                    sendFailedResponseMessage(e.getMessage());
                } catch (NoSuchMethodException e) {
                    sendFailedResponseMessage("transaction type is invalid");
                } catch (InvocationTargetException e) {
                    System.out.println(e.getCause().getMessage());
                    sendFailedResponseMessage(e.getCause().getMessage());

                } catch (IllegalAccessException e) {
                    sendFailedResponseMessage("transaction type is invalid");
                }
                if(! finishSuccess){
                    serverFileManager.addFinishTransactionFailedLog(request , terminalId , terminalType);
                }
            }
        }  catch (IOException e) {
            System.out.println("Close connection");
        } finally {
            try {
                terminalSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

