package server;

;import jdk.nashorn.api.scripting.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dotin School1 on 4/9/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        ServerFileManager serverFileManager = new ServerFileManager();
        List<Deposit> deposits = serverFileManager.getDeposits();
    }
}
