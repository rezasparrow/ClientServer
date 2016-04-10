package server;

import org.json.simple.ItemList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dotin School1 on 4/10/2016.
 */
public class ServerFileManager {

    private String port;
    private String outLog;
    private List<Deposit> deposits;

    public ServerFileManager() throws IOException, ParseException {

        this.deposits = new ArrayList<Deposit>();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("core.json"));

        port = jsonObject.get("port").toString();
        outLog = jsonObject.get("outLog").toString();

        JSONArray deposits = (JSONArray) jsonObject.get("deposits");
        for (Object depositObject : deposits) {
            JSONObject deposit = (JSONObject) depositObject;

            String customer = (String) deposit.get("customer");
            System.out.println(customer);

            String id = (String) deposit.get("id");
            System.out.println(id);

            Integer initialBalance = Integer.parseInt( deposit.get("initialBalance").toString().replace("," , ""));
            System.out.println(initialBalance);

            Integer upperBalance = Integer.parseInt( deposit.get("upperBound").toString().replace("," , ""));
            System.out.println(upperBalance);

            this.deposits.add(new Deposit(customer ,id ,initialBalance ,upperBalance));
        }
    }

    public String getPort() {
        return port;
    }

    public String getOutLog() {
        return outLog;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }
}
