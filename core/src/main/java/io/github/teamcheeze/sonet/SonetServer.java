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

import io.github.teamcheeze.jaw.reflection.FieldAccessor;
import io.github.teamcheeze.jaw.reflection.MethodAccessor;
import io.github.teamcheeze.jaw.reflection.ReflectionException;
import io.github.teamcheeze.sonet.network.component.Server;
import io.github.teamcheeze.sonet.network.data.buffer.StaticSonetBuffer;
import io.github.teamcheeze.sonet.network.data.packet.PacketNotFoundException;
import io.github.teamcheeze.sonet.network.data.packet.SonetDataDeserializer;
import io.github.teamcheeze.sonet.network.data.packet.SonetPacket;
import io.github.teamcheeze.sonet.network.handlers.*;
import io.github.teamcheeze.sonet.network.util.net.AddressUtils;
import io.github.teamcheeze.sonet.network.util.net.SonetServerAddress;
import io.github.teamcheeze.sonet.sample.SamplePacket;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import java.util.concurrent.ArrayBlockingQueue;
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
    private final List<ServerPacketHandler<? extends SonetPacket>> packetHandlers = new ArrayList<>();
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
    public void addPacketHandler(ServerPacketHandler<? extends SonetPacket> handler) {
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
    public void removePacketHandler(ServerPacketHandler<? extends SonetPacket> handler) {
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
    public List<ServerPacketHandler<? extends SonetPacket>> getPacketHandlers() {
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

    private Runnable startRunnable() {
        return startRunnable(null);
    }

    private Runnable startRunnable(CompletableFuture<Void> future) {
        return () -> {
            try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
                if (future != null) {
                    future.complete(null);
                }
                serverChannel.bind(new InetSocketAddress(AddressUtils.localAddress, address.getPort()));
                serverChannel.configureBlocking(false);
                Selector selector = Selector.open();
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
                while (valid) {
                    int select = selector.select();
                    if (select == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            SocketChannel client = serverChannel.accept();
                            client.configureBlocking(false);
                            for (SonetConnectionHandler handler : clientHandlers) {
                                handler.handle(client);
                            }
                            client.register(selector, SelectionKey.OP_READ);
                            clients.add(client);
                        } else if (key.isReadable()) {
                            // This is the client channel
                            SocketChannel channel = (SocketChannel) key.channel();

                            // Declare ByteBuffers - header and body
                            ByteBuffer header = ByteBuffer.allocate(4);
                            ByteBuffer body = null;

                            // Declare the size of the body
                            int size;
                            try {
                                channel.read(header);
                                header.flip();
                                size = header.getInt();
                                if (size <= 0) {
                                    return;
                                }
                                body = ByteBuffer.allocate(size);
                                channel.read(body);
                                header.clear();
                            } catch (IOException e) {
                                key.cancel();
                                clients.remove(channel);
                                if (body != null) {
                                    body.clear();
                                }
                                return;
                            }
                            SonetPacket received;
                            try {
                                received = SonetDataDeserializer.deserializePacket(body);
                            } catch (PacketNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            if (received == null) {
                                System.out.println("INVALID PACKET");
                                return;
                            }

                            SonetPacket packet = null;
                            for (ServerPacketHandler<? extends SonetPacket> handler : packetHandlers) {
                                @SuppressWarnings("unchecked")
                                Class<? extends SonetPacket> packetClass = (Class<? extends SonetPacket>) new MethodAccessor<>(handler, "getType").setDeclaringClass(AbstractHandler.class).invoke();
                                if (packetClass == received.getClass()) {
                                    try {
                                        new MethodAccessor<>(handler, "handle").setDeclaringClass(AbstractHandler.class).invoke(received);
                                    } catch (ReflectionException e) {
                                        ((InvocationTargetException) e.raw.getCause()).printStackTrace();
                                        return;
                                    }
                                    if (handler.packetSent) {
                                        try {
                                            packet = (SonetPacket) new FieldAccessor<>(handler, "packet").setDeclaringClass(ServerPacketHandler.class).get();
                                        } catch (ReflectionException e) {
                                            e.raw.printStackTrace();
                                        }
                                        break;
                                    }
                                }
                            }
                            ByteBuffer bodyBuf = (packet == null) ? received.serialize() : packet.serialize();
                            ByteBuffer headBuf = ByteBuffer.allocate(4);
                            headBuf.putInt(bodyBuf.capacity());
                            headBuf.flip();
                            channel.write(new ByteBuffer[] { headBuf, bodyBuf });
                            bodyBuf.clear();
                            headBuf.clear();
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public CompletableFuture<Void> startAsync() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        new Thread(startRunnable(future)).start();
        return future;
    }

    /**
     * Execute start with the default value of true
     */
    @Override
    public void start() {
        startRunnable().run();
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
