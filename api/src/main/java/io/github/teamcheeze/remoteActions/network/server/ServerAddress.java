package io.github.teamcheeze.remoteActions.network.server;

import io.github.teamcheeze.remoteActions.network.Address;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

public class ServerAddress extends Address implements Serializable {
    public InetAddress ip;
    public int port;
    public ServerAddress() {  }
    public ServerAddress(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerAddress address = (ServerAddress) o;
        return port == address.port && ip.getHostAddress().equals(address.ip.getHostAddress());
    }

    public boolean matchIp(@Nullable ServerAddress address) {
        if (address == null) return false;
        return this.ip.getHostAddress().equals(address.ip.getHostAddress());
    }

    public boolean matchIp(@Nullable InetAddress address) {
        if (address == null) return false;
        return this.ip.getHostAddress().equals(address.getHostAddress());
    }


    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }


    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
