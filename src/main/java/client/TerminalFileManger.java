package client;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class TerminalFileManger {

    private BufferedWriter logFile;
    private Terminal terminal;

    public TerminalFileManger() throws ParserConfigurationException, IOException, SAXException {

        File inputFile = new File("terminal.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();


        Node rootElement = doc.getDocumentElement();
        String id = rootElement.getAttributes().getNamedItem("id").getNodeValue();
        String type = rootElement.getAttributes().getNamedItem("type").getNodeValue();

        Node serverInfoNode = doc.getElementsByTagName("server").item(0);
        String serverIp = serverInfoNode.getAttributes().getNamedItem("ip").getNodeValue();
        Integer serverPort = Integer.parseInt(serverInfoNode.getAttributes().getNamedItem("port").getNodeValue());
        ServerInformation serverInformation = new ServerInformation(serverIp, serverPort);

        Node outLog = doc.getElementsByTagName("outLog").item(0);
        String outLogPath = outLog.getAttributes().getNamedItem("path").getNodeValue();
        this.logFile = new BufferedWriter(new FileWriter(outLogPath));

        this.terminal = new Terminal(id, type, serverInformation , this);

        NodeList transactions = doc.getElementsByTagName("transaction");
        System.out.println("----------------------------");
        for (int i = 0; i < transactions.getLength(); i++) {
            Node transaction = transactions.item(i);

            if (transaction.getNodeType() == Node.ELEMENT_NODE) {
                String transactionId = transaction.getAttributes().getNamedItem("id").getNodeValue();
                String transactionType = transaction.getAttributes().getNamedItem("type").getNodeValue();
                String amount = transaction.getAttributes().getNamedItem("amount").getNodeValue();
                String deposit = transaction.getAttributes().getNamedItem("deposit").getNodeValue();

                terminal.addTransaction(new Transaction(transactionId, transactionType, Integer.parseInt(amount.replace(",", "")), deposit));
            }
        }


    }


    public Terminal getTerminal() {
        return terminal;
    }

    public synchronized void addLog(String transaction , String message) throws IOException {
        Long currentTime = System.currentTimeMillis();
        logFile.write(String.format(" time=\"%s\"\t" +
                "transaction =  %s\t"+
                "serverMessage = %s\t" ,currentTime.toString() , transaction , message));
        logFile.newLine();

    }
    public void close() throws IOException {
        logFile.flush();
        logFile.close();
    }
}
