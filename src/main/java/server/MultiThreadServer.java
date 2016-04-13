package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.TransactionInvalidException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class MultiThreadServer implements Runnable   {
    private Socket terminalSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private List<Deposit> depositList;
    private ServerFileManager serverFileManager;

    private String terminalType;
    private String terminalId;


    public MultiThreadServer(ServerSocket serverSocket, List<Deposit> deposits , ServerFileManager serverFileManager) throws IOException, ParseException {
        terminalSocket = serverSocket.accept();
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

                String request = dataInputStream.readUTF();
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
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        terminalSocket.close();
    }
}

