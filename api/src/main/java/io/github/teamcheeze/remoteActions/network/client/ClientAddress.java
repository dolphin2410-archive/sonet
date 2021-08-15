package io.github.teamcheeze.remoteActions.network.client;

import io.github.teamcheeze.remoteActions.network.Address;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.util.Objects;

public class ClientAddress extends Address {
    private InetAddress ip;
    public ClientAddress() {

    }

    public ClientAddress(InetAddress ip) {
        this.setIp(ip);
    }

    public boolean matchIp(@Nullable ClientAddress address) {
        if (address == null) return false;
        return this.ip.getHostAddress().equals(address.ip.getHostAddress());
    }

    public boolean matchIp(@Nullable InetAddress address) {
        if (address == null) return false;
        return this.ip.getHostAddress().equals(address.getHostAddress());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientAddress that = (ClientAddress) o;
        return Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }

    @Override
    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    @Override
    public InetAddress getIp() {
        return this.ip;
    }
}
