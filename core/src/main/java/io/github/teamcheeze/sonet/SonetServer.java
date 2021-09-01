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
import io.github.teamcheeze.sonet.network.*;
import io.github.teamcheeze.sonet.network.component.Server;
import io.github.teamcheeze.sonet.network.data.SonetBuffer;
import io.github.teamcheeze.sonet.network.data.SonetPacket;
import io.github.teamcheeze.sonet.network.handlers.ServerPacketHandler;
import io.github.teamcheeze.sonet.network.handlers.SonetConnectionHandler;
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
    public void removeClientHandler(SonetConnectionHandler handler) {
        clientHandlers.remove(handler);
    }

    @Override
    public void removePacketHandler(ServerPacketHandler handler) {
        packetHandlers.remove(handler);
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
    public void start(boolean block) {
        Runnable r = () -> {
            try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
                serverChannel.bind(new InetSocketAddress(AddressUtils.localAddress, address.getPort()));
                serverChannel.configureBlocking(false);
                Selector selector = Selector.open();
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
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
                            SonetPacket packet = null;
                            SonetPacket received;
                            try {
                                received = PacketDeserializer.deserialize(packetType, body);
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
                            sonetHeader.writeByte(packetType);
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

        if (block) {
            r.run();
        } else {
            new Thread(r).start();
        }
    }

    /**
     * Execute start with the default value of true
     */
    @Override
    public void start() {
        start(true);
    }

    @Override
    public List<SocketChannel> getClients() {
        return clients;
    }
}
