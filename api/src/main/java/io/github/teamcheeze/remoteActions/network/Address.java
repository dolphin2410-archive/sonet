package io.github.teamcheeze.remoteActions.network;

import java.net.Inet4Address;

public class Address {
    public Inet4Address ipv4;
    public int port;
    public Address() {  }
    public Address(Inet4Address ipv4, int port) {
        this.ipv4 = ipv4;
        this.port = port;
    }

    public Inet4Address getIpv4() {
        return ipv4;
    }

    public void setIpv4(Inet4Address ipv4) {
        this.ipv4 = ipv4;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
