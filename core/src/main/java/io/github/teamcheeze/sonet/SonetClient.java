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

import io.github.teamcheeze.sonet.network.PacketInvoker;
import io.github.teamcheeze.sonet.network.component.Client;
import io.github.teamcheeze.sonet.network.data.buffer.SonetBuffer;
import io.github.teamcheeze.sonet.network.data.buffer.StaticSonetBuffer;
import io.github.teamcheeze.sonet.network.data.packet.PacketNotFoundException;
import io.github.teamcheeze.sonet.network.data.packet.SonetDataDeserializer;
import io.github.teamcheeze.sonet.network.data.packet.SonetPacket;
import io.github.teamcheeze.sonet.network.handlers.ClientPacketHandler;
import io.github.teamcheeze.sonet.network.util.SonetBoolean;
import io.github.teamcheeze.sonet.network.util.net.AddressUtils;
import io.github.teamcheeze.sonet.network.util.net.SonetClientAddress;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class SonetClient implements Client {
    private final SonetClientAddress address;
    private final UUID uuid;
    private boolean valid;
    private PacketInvoker invoker = null;
    private SocketChannel channel = null;
    private final AtomicReference<Selector> selector = new AtomicReference<>();
    List<ClientPacketHandler> packetHandlers = new ArrayList<>();

    public SonetClient() {
        this.uuid = UUID.randomUUID();
        this.address = new SonetClientAddress(AddressUtils.localAddress);
        this.valid = true;
    }

    @Override
    public CompletableFuture<Void> connectAsync(InetAddress ip, int port) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        ArrayBlockingQueue<Runnable> readQueue = new ArrayBlockingQueue<>(1);
        invoker = packet -> {
            CompletableFuture<SonetPacket> result = new CompletableFuture<>();
            write(packet);
            try {
                readQueue.put(() -> result.complete(read()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return result;
        };
        new Thread(() -> {
            try {
                channel = SocketChannel.open();
                if (channel.connect(new InetSocketAddress(AddressUtils.localAddress, port))) {
                    channel.configureBlocking(false);
                    future.complete(null);
                }
                selector.set(Selector.open());
                channel.register(selector.get(), SelectionKey.OP_READ, SelectionKey.OP_WRITE);
                while (valid) {
                    selector.get().select();
                    Iterator<SelectionKey> iterator = selector.get().selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            Runnable runnable = readQueue.poll();
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    }
                }
                selector.get().close();
            } catch (IOException e) {
                abort();
                System.exit(0);
            }
        }).start();
        return future;
    }

    private SonetPacket read() {
        try {
            // [ 4bytes = size of packet _int ]
            ByteBuffer head = ByteBuffer.allocate(4);
            channel.read(head);
            head.flip();
            int size = head.getInt();
            ByteBuffer body = ByteBuffer.allocate(size);
            channel.read(body);
            body.flip();
            SonetPacket packet = SonetDataDeserializer.deserializePacket(body);
            head.clear();
            body.clear();
            return packet;
        } catch (IOException | PacketNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(SonetPacket packet) {
        try {

            // The body ByteBuffer
            ByteBuffer serializedPacket = packet.serialize();

            ByteBuffer header = ByteBuffer.allocate(4);

            header.putInt(serializedPacket.capacity());

            header.flip();

            channel.write(new ByteBuffer[] { header, serializedPacket });

            // Clear the used body ByteBuffer
            serializedPacket.clear();

            header.clear();
        } catch (IOException e) {
            abort();
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
                setValid(false);
                if (selector.get() != null) {
                    for (SelectionKey key : selector.get().selectedKeys()) {
                        key.cancel();
                    }
                    selector.get().wakeup();
                }
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
    public byte[] sendRawBytes() {
        //TODO finish. DefaultProtocol.HTTP
        return new byte[0];
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
