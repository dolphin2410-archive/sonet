package io.github.teamcheeze.remoteActions.network;

import java.net.Inet4Address;
import java.util.Objects;

public class Address {
    public Inet4Address ipv4;
    public int port;
    public Address() {  }
    public Address(Inet4Address ipv4, int port) {
        this.ipv4 = ipv4;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return port == address.port && Objects.equals(ipv4, address.ipv4);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipv4, port);
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
