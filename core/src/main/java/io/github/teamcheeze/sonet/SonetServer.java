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

package io.github.teamcheeze.sonet;

import io.github.teamcheeze.sonet.network.*;
import io.github.teamcheeze.sonet.network.component.Server;
import io.github.teamcheeze.sonet.network.data.SonetBuffer;
import io.github.teamcheeze.sonet.network.handlers.SonetConnectionHandler;
import io.github.teamcheeze.sonet.network.handlers.SonetPacketHandler;
import io.github.teamcheeze.sonet.network.util.AddressUtils;
import io.github.teamcheeze.sonet.network.util.SonetServerAddress;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The new server, with optimized nio features.
 * @author dolphin2410
 */
public class SonetServer implements Server {
    private boolean valid;
    private final List<SocketChannel> clients = new ArrayList<>();
    private final UUID id = UUID.randomUUID();
    private final SonetServerAddress address;
    private final List<SonetConnectionHandler> clientHandlers = new ArrayList<>();
    private final List<SonetPacketHandler> packetHandlers = new ArrayList<>();
    public SonetServer(int port) {
        this.address = new SonetServerAddress(AddressUtils.localAddress, port);
        this.valid = true;
    }
    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public void addClientHandler(SonetConnectionHandler handler) {
        clientHandlers.add(handler);
    }

    @Override
    public void addPacketHandler(SonetPacketHandler handler) {
        packetHandlers.add(handler);
    }

    @Override
    public void removeClientHandler(SonetConnectionHandler handler) {
        clientHandlers.remove(handler);
    }

    @Override
    public void removePacketHandler(SonetPacketHandler handler) {
        packetHandlers.remove(handler);
    }

    @Override
    public List<SonetConnectionHandler> getClientHandlers() {
        return clientHandlers;
    }

    @Override
    public List<SonetPacketHandler> getPacketHandlers() {
        return packetHandlers;
    }

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @Override
    public @NotNull SonetServerAddress getAddress() {
        return address;
    }

    /**
     * This method will be blocked util (valid = false).
     */
    @Override
    public void start() {
        try(ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(AddressUtils.localAddress, address.getPort()));
            serverChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
                try {
                    while (valid) {
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        selector.select();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            iterator.remove();
                            if (key.isAcceptable()) {
                                ServerSocketChannel server = (ServerSocketChannel) key.channel();
                                SocketChannel client = server.accept();
                                client.configureBlocking(false);
                                for (SonetConnectionHandler handler : clientHandlers) {
                                    handler.handle(client);
                                }
                                client.register(selector, SelectionKey.OP_READ);
                                clients.add(client);
                            } else if (key.isReadable()) {
                                SocketChannel channel = (SocketChannel) key.channel();
                                ByteBuffer header;
                                ByteBuffer body = null;
                                int size;
                                byte packetType;
                                try {
                                    // The header should contain
                                    // 4-byte varInt - length
                                    // byte - packet type
                                    // Total five bytes
                                    header = ByteBuffer.allocate(5);
                                    channel.read(header);
                                    SonetBuffer sonetBuffer = SonetBuffer.load(header);
                                    size = sonetBuffer.readInt();
                                    if (size <= 0) {
                                        continue;
                                    }
                                    packetType = sonetBuffer.readByte();
                                    body = ByteBuffer.allocate(size);
                                    channel.read(body);
                                } catch (IOException e) {
                                    key.cancel();
                                    clients.remove(channel);
                                    if (body != null) {
                                        body.clear();
                                    }
                                    continue;
                                }
                                for (SonetPacketHandler handler : packetHandlers) {
                                    try {
                                        handler.handle(PacketDeserializer.deserialize(packetType, body));
                                    } catch (PacketNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                                channel.write(body);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SocketChannel> getClients() {
        return clients;
    }

    public void handleClients(Consumer<List<SocketChannel>> consumer) {
        consumer.accept(clients);
    }
}
