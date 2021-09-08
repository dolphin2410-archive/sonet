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

import io.github.dolphin2410.jaw.reflection.FieldAccessor;
import io.github.dolphin2410.jaw.reflection.ReflectionException;
import io.github.teamcheeze.sonet.network.component.Server;
import io.github.teamcheeze.sonet.network.data.packet.SonetDataDeserializer;
import io.github.teamcheeze.sonet.network.data.packet.PacketNotFoundException;
import io.github.teamcheeze.sonet.network.data.buffer.SonetBuffer;
import io.github.teamcheeze.sonet.network.data.packet.SonetPacket;
import io.github.teamcheeze.sonet.network.handlers.RawByteHandler;
import io.github.teamcheeze.sonet.network.handlers.ServerPacketHandler;
import io.github.teamcheeze.sonet.network.handlers.SonetConnectionHandler;
import io.github.teamcheeze.sonet.network.util.net.AddressUtils;
import io.github.teamcheeze.sonet.network.util.net.SonetServerAddress;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * The new server, with optimized nio features.
 *
 * @author dolphin2410
 */
public class SonetServer implements Server {
    private boolean valid;
    private final List<SocketChannel> clients = new ArrayList<>();
    private final UUID id = UUID.randomUUID();
    private final SonetServerAddress address;
    private final List<SonetConnectionHandler> clientHandlers = new ArrayList<>();
    private final List<ServerPacketHandler> packetHandlers = new ArrayList<>();
    private final List<RawByteHandler> rawDataHandlers = new ArrayList<>();

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
    public void addPacketHandler(ServerPacketHandler handler) {
        packetHandlers.add(handler);
    }

    @Override
    public void addRawDataHandler(RawByteHandler handler) {
        rawDataHandlers.add(handler);
    }

    @Override
    public void removeClientHandler(SonetConnectionHandler handler) {
        clientHandlers.remove(handler);
    }

    @Override
    public void removePacketHandler(ServerPacketHandler handler) {
        packetHandlers.remove(handler);
    }

    @Override
    public void removeRawDataHandler(RawByteHandler handler) {
        rawDataHandlers.remove(handler);
    }

    @Override
    public List<SonetConnectionHandler> getClientHandlers() {
        return clientHandlers;
    }

    @Override
    public List<ServerPacketHandler> getPacketHandlers() {
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

    @Override
    public CompletableFuture<Void> startAsync() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Runnable r = () -> {
            try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
                future.complete(null);
                serverChannel.bind(new InetSocketAddress(AddressUtils.localAddress, address.getPort()));
                serverChannel.configureBlocking(false);
                Selector selector = Selector.open();
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
                while (valid) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    selector.select();
                    while (iterator.hasNext()) {
                        SelectionKey key;
                        key = iterator.next();
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
                            try {
                                header = ByteBuffer.allocate(4);
                                channel.read(header);
                                SonetBuffer sonetBuffer = SonetBuffer.loadReset(header);
                                size = sonetBuffer.readInt();
                                if (size <= 0) {
                                    continue;
                                }
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
                            SonetPacket packet = null;
                            SonetPacket received;
                            try {
                                received = SonetDataDeserializer.deserializePacket(body);
                            } catch (PacketNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                            for (ServerPacketHandler handler : packetHandlers) {
                                handler.handle(received);
                                if (handler.packetSent) {
                                    try {
                                        packet = (SonetPacket) new FieldAccessor<>(handler, "packet").setDeclaringClass(ServerPacketHandler.class).get();
                                    } catch (ReflectionException e) {
                                        e.raw.printStackTrace();
                                    }
                                    break;
                                }
                            }
                            SonetBuffer toSend = SonetBuffer.load((packet == null) ? received.serialize() : packet.serialize());
                            SonetBuffer sonetHeader = new SonetBuffer();
                            sonetHeader.writeInt(toSend.toBuffer().capacity());
                            sonetHeader.updateBuffer();
                            channel.write(sonetHeader.toBuffer());
                            channel.write(toSend.toBuffer());
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(r).start();
        return future;
    }

    /**
     * Execute start with the default value of true
     */
    @Override
    public void start() {
        startAsync().join();
    }

    @Override
    public List<SocketChannel> getClients() {
        return clients;
    }

    @Override
    public List<RawByteHandler> getRawDataHandlers() {
        return rawDataHandlers;
    }
}
