package server;

import org.json.simple.ItemList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.rmi.runtime.Log;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dotin School1 on 4/10/2016.
 */
public class ServerFileManager {

    private Integer port;

    private List<Deposit> deposits;
    private BufferedWriter logFile;
    private Object lockLogFile = new Object();
    public ServerFileManager() throws IOException, ParseException {

        this.deposits = new ArrayList<Deposit>();


        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("core.json"));

        port = Integer.parseInt(jsonObject.get("port").toString());
        String outLog = jsonObject.get("outLog").toString();

        JSONArray deposits = (JSONArray) jsonObject.get("deposits");
        for (Object depositObject : deposits) {
            JSONObject deposit = (JSONObject) depositObject;

            String customer = (String) deposit.get("customer");
            String id =  deposit.get("id").toString();
            Integer initialBalance = Integer.parseInt( deposit.get("initialBalance").toString().replace("," , ""));
            Integer upperBalance = Integer.parseInt( deposit.get("upperBound").toString().replace("," , ""));

            this.deposits.add(new Deposit(customer ,id ,initialBalance ,upperBalance));
        }
        initLogger(outLog);
    }

    private void initLogger(String logFilePath) throws IOException {
        this.logFile = new BufferedWriter(new FileWriter(logFilePath));

    }

    public Integer getPort() {
        return port;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }

    private void addLog(String transaction , String terminalId, String terminalType , String mode  , String status) throws IOException {
        synchronized (logFile){
            logFile.write(String.format("mode=\"%s\" status=\"%s\" time = \"%s\"  " , mode , status ,  System.currentTimeMillis()   ));
            logFile.write(String.format("transaction=  %s " , transaction));
            logFile.write(String.format("terminalId=\"%s\"  terminalType=\"%s\"" , terminalId , terminalType));
            logFile.newLine();
            logFile.flush();
        }
    }


    public void addStartTransactionLog(String transaction , String terminalId, String terminalType) throws IOException {
        addLog(transaction , terminalId , terminalType , "start" , "");
    }

    public void addFinishTransactionFailedLog(String transaction , String terminalId, String terminalType) throws IOException {
        addLog(transaction , terminalId , terminalType , "finish" , "failed");
    }

    public void addFinishTransactionSuccessLog(String transaction , String terminalId, String terminalType) throws IOException {
        addLog(transaction , terminalId , terminalType , "finish" , "success");
    }


    public void close() throws IOException {
        logFile.flush();
        logFile.close();
    }

}
