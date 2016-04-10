package client;

/**
 * Created by Dotin School1 on 4/10/2016.
 */
public class ServerInformation {
    private String ip;
    private Integer port;

    public ServerInformation(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }
}
