package io.github.teamcheeze.remoteActions.network;

import java.io.Serializable;
import java.net.InetAddress;

public abstract class Address implements Serializable {
    public abstract InetAddress getIp();
    public abstract void setIp(InetAddress ip);
}
