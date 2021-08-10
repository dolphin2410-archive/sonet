package io.github.teamcheeze.remoteActions.network;

import java.net.InetAddress;

public abstract class Address {
    public abstract InetAddress getIp();
    public abstract void setIp(InetAddress ip);
}
