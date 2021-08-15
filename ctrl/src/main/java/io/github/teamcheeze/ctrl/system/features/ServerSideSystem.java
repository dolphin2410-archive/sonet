package io.github.teamcheeze.ctrl.system.features;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.util.Machine;

import java.io.Serializable;

public interface ServerSideSystem extends Serializable {
    Connection getConnection();
    default boolean isServer() {
        return getConnection().getServer().getAddress().matchIp(Machine.localIpAddress);
    }
    default boolean isClient() {
        return getConnection().getServer().getAddress().matchIp(Machine.localIpAddress);
    }
}
