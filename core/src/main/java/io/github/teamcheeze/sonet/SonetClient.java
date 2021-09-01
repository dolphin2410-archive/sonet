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
import io.github.teamcheeze.sonet.network.component.Client;
import io.github.teamcheeze.sonet.network.data.SonetBuffer;
import io.github.teamcheeze.sonet.network.data.SonetPacket;
import io.github.teamcheeze.sonet.network.handlers.ClientPacketHandler;
import io.github.teamcheeze.sonet.network.util.AddressUtils;
import io.github.teamcheeze.sonet.network.util.SonetClientAddress;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class SonetClient implements Client {
    private final SonetClientAddress address;
    private final UUID uuid;
    private boolean valid;
    private PacketInvoker invoker = null;
    private SocketChannel channel = null;
    List<ClientPacketHandler> packetHandlers = new ArrayList<>();

    public SonetClient() {
        this.uuid = UUID.randomUUID();
        this.address = new SonetClientAddress(AddressUtils.localAddress);
        this.valid = true;
    }

    @Override
    public CompletableFuture<Void> connectAsync(InetAddress ip, int port) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        SonetBoolean readAvailable = new SonetBoolean(false);
        invoker = packet -> {
            CompletableFuture<SonetPacket> result = new CompletableFuture<>();
            write(packet);
            readAvailable.triggerOnceOnTrue(() -> {
                result.complete(read());
            });
            return result;
        };
        new Thread(() -> {
            try {
                channel = SocketChannel.open();
                if (channel.connect(new InetSocketAddress(AddressUtils.localAddress, port))) {
                    channel.configureBlocking(false);
                    future.complete(null);
                }
                Selector selector = Selector.open();
                channel.register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE);
                while (true) {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            readAvailable.set(true);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return future;
    }

    private SonetPacket read() {
        try {
            // [ 4bytes = size of packet _int ] [ 1byte = type of packet _byte ]
            ByteBuffer returnedHeaderBuffer = ByteBuffer.allocate(5);
            channel.read(returnedHeaderBuffer);
            SonetBuffer returnedSonetHeader = SonetBuffer.load(returnedHeaderBuffer);
            int size = returnedSonetHeader.readInt();
            byte type = returnedSonetHeader.readByte();
            ByteBuffer returnedBodyBuffer = ByteBuffer.allocate(size);
            channel.read(returnedBodyBuffer);
            return PacketDeserializer.deserialize(type, returnedBodyBuffer);
        } catch (IOException | PacketNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(SonetPacket packet) {
        try {
            // [ 4bytes = size of packet _int ] [ 1byte = type of packet _byte ]
            SonetBuffer sonetHeader = new SonetBuffer();

            // The body ByteBuffer
            ByteBuffer serializedPacket = packet.serialize();

            // Write data to header
            sonetHeader.writeInt(serializedPacket.capacity());
            sonetHeader.writeByte(PacketRegistry.getType(packet.getClass()));

            // Send header to the server
            channel.write(sonetHeader.toBuffer());

            // Send body to the server
            channel.write(serializedPacket);

            // Clear the used body ByteBuffer
            serializedPacket.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<SonetPacket> sendPacketAsync(SonetPacket packet) {
        if (invoker != null) {
            return invoker.invoke(packet);
        }
        throw new RuntimeException("There is no invoker registered");
    }

    @Override
    public SonetPacket sendPacket(SonetPacket packet) {
        return sendPacketAsync(packet).join();
    }

    @Override
    public @NotNull UUID getId() {
        return uuid;
    }

    @Override
    public @NotNull SonetClientAddress getAddress() {
        return address;
    }

    @Override
    public void abort() {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addPacketHandler(ClientPacketHandler handler) {
        packetHandlers.add(handler);
    }

    @Override
    public void removePacketHandler(ClientPacketHandler handler) {
        packetHandlers.remove(handler);
    }

    @Override
    public List<ClientPacketHandler> getPacketHandlers() {
        return packetHandlers;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
