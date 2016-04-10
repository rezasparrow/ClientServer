package client;

/**
 * Created by Dotin School1 on 4/10/2016.
 */
public class ServerInformation {
    private String ip;
    private String port;

    public ServerInformation(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }
}
