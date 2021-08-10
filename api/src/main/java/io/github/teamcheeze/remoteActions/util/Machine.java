package io.github.teamcheeze.remoteActions.util;

import io.github.teamcheeze.remoteActions.network.server.ServerAddress;
import org.jetbrains.annotations.NotNull;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utilities for the current machine
 */
public class Machine {
    @NotNull
    @Deprecated
    public static ServerAddress localAddress;
    @NotNull
    public static InetAddress localIpAddress;

    static {
        try {
            localIpAddress = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try {
            localAddress = new ServerAddress(Inet4Address.getLocalHost(), 0);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
