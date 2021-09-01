/*
 * Sonet
 * Copyright (C) 2021 dolphin2410
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.teamcheeze.sonet.network.component;

import io.github.teamcheeze.sonet.network.data.SonetPacket;
import io.github.teamcheeze.sonet.network.handlers.ClientPacketHandler;
import io.github.teamcheeze.sonet.network.handlers.SonetPacketHandler;
import io.github.teamcheeze.sonet.network.util.SonetClientAddress;
import org.jetbrains.annotations.NotNull;
import java.net.InetAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * The client interface. You can inherit this class for your custom client.
 */
public interface Client {
    /**
     * Connects to the server asynchronously
     * @param ip The ip address to connect to
     * @param port The port to connect to
     * @return The completable future. All client actions must run after the connection is done
     */
    CompletableFuture<Void> connectAsync(InetAddress ip, int port);

    default void connect(InetAddress ip, int port) {
        connectAsync(ip, port).join();
    }

    /**
     * Send a SonetPacket object to the server
     * @param packet The packet to send
     * @return The returned packet
     */
    SonetPacket sendPacket(SonetPacket packet);

    /**
     * The virtual ID that is used to identify the specific client from the server
     * @return The id
     */
    @NotNull UUID getId();

    /**
     * The physical address of the machine
     * @return The address
     */
    @NotNull SonetClientAddress getAddress();

    /**
     * Aborts the current connection
     */
    void abort();

    /**
     * Checks whether the client is valid or not. If aborted, or if the channel is closed forcibly by an external factor, it will return false, and when false, the client will shutdown and exit
     * @return validity
     */
    boolean isValid();

    /**
     * Sets whether it is valid
     * @param valid validity
     */
    void setValid(boolean valid);

    void addPacketHandler(ClientPacketHandler handler);

    void removePacketHandler(ClientPacketHandler handler);

    List<ClientPacketHandler> getPacketHandlers();

}
