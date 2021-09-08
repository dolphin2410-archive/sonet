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

import io.github.teamcheeze.sonet.network.handlers.RawByteHandler;
import io.github.teamcheeze.sonet.network.handlers.ServerPacketHandler;
import io.github.teamcheeze.sonet.network.handlers.SonetConnectionHandler;
import io.github.teamcheeze.sonet.network.util.net.SonetServerAddress;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * The sonet server interface. Inherit this class for a custom server
 */
public interface Server {
    /**
     * Checks if the server is valid and running. Once set false, the server loop will cancel.
     *
     * @return validity
     */
    boolean isValid();

    /**
     * Sets whether the server is valid or not. Cannot set to true once set to false
     *
     * @param valid validity
     */
    void setValid(boolean valid);

    /**
     * An id to identify the server
     *
     * @return Id
     */
    UUID getId();

    /**
     * Gets the server's physical address, and the current bound port
     *
     * @return server's address
     */
    SonetServerAddress getAddress();

    /**
     * Starts the server, non-blocking
     */
    CompletableFuture<Void> startAsync();

    /**
     * Start the server, blocking
     */
    void start();

    /**
     * Add a client connection handler
     *
     * @param handler connectionHandler
     */
    void addClientHandler(SonetConnectionHandler handler);

    /**
     * Adds a packet input handler. Activates once the packet is recieved
     *
     * @param handler packetHandler
     */
    void addPacketHandler(ServerPacketHandler handler);

    void addRawDataHandler(RawByteHandler handler);

    /**
     * Removes a client connection handler
     *
     * @param handler connectionHandler
     */
    void removeClientHandler(SonetConnectionHandler handler);

    /**
     * Removes a packet input handler
     *
     * @param handler packetHandler
     */
    void removePacketHandler(ServerPacketHandler handler);

    void removeRawDataHandler(RawByteHandler handler);

    /**
     * Gets all client handlers
     *
     * @return client handlers
     */
    List<SonetConnectionHandler> getClientHandlers();

    /**
     * Gets all packet handlers
     *
     * @return packet handlers
     */
    List<ServerPacketHandler> getPacketHandlers();

    /**
     * Gets list of clients
     * @return list of clients
     */
    List<SocketChannel> getClients();

    List<RawByteHandler> getRawDataHandlers();
}
