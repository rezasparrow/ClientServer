package client;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Dotin School1 on 4/9/2016.
 */
public class TerminalFileManger {
    public static Terminal parseInput() throws ParserConfigurationException, IOException, SAXException {

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
        String serverPort = serverInfoNode.getAttributes().getNamedItem("port").getNodeValue();
        ServerInformation serverInformation = new ServerInformation(serverIp, serverPort);

        Node outLog = doc.getElementsByTagName("outLog").item(0);
        String outLogPath = outLog.getAttributes().getNamedItem("path").getNodeValue();

        Terminal terminal = new Terminal(id,type, serverInformation , outLogPath);

        NodeList transactions = doc.getElementsByTagName("transaction");
        System.out.println("----------------------------");
        for (int i = 0; i < transactions.getLength(); i++) {
            Node transaction = transactions.item(i);

            if (transaction.getNodeType() == Node.ELEMENT_NODE) {
                String transactionId = transaction.getAttributes().getNamedItem("id").getNodeValue();
                String transactionType = transaction.getAttributes().getNamedItem("type").getNodeValue();
                String amount = transaction.getAttributes().getNamedItem("amount").getNodeValue();
                String deposit = transaction.getAttributes().getNamedItem("deposit").getNodeValue();

                terminal.addTransaction(new Transaction(transactionId ,  transactionType , Integer.parseInt(amount.replace("," , "")) , deposit));
            }
        }


        return terminal;
    }
}
