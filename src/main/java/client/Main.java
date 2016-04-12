package client;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Dotin School1 on 4/9/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        TerminalFileManger terminalFileManger = new TerminalFileManger();
        Terminal terminal = terminalFileManger.getTerminal();
        terminal.connectToServer();
        for(Transaction transaction : terminal.getTransactions()){
            System.out.println(terminal.sendData(transaction.toString()));
            return;
        }
        terminal.closeConnection();
    }
}
