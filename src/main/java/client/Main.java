package client;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Dotin School1 on 4/9/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        TerminalFileManger.parseInput();
    }
}
