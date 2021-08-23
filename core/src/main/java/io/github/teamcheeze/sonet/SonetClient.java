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
import io.github.teamcheeze.sonet.network.PacketRegistry;
import io.github.teamcheeze.sonet.network.component.Client;
import io.github.teamcheeze.sonet.network.data.SonetBuffer;
import io.github.teamcheeze.sonet.network.data.SonetPacket;
import io.github.teamcheeze.sonet.network.util.AddressUtils;
import io.github.teamcheeze.sonet.network.util.SonetClientAddress;
import io.github.teamcheeze.sonet.network.util.SonetServerAddress;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SonetClient implements Client {
    private final SonetClientAddress address;
    private final UUID uuid;
    private boolean valid;
    private PacketInvoker invoker = null;
    private SocketChannel channel = null;

    public SonetClient() {
        this.uuid = UUID.randomUUID();
        this.address = new SonetClientAddress(AddressUtils.localAddress);
        System.out.println("Created sonetClient instance. ID: " + uuid);
        this.valid = true;
    }

    @Override
    public CompletableFuture<Void> connect(InetAddress ip, int port) {
        SonetServerAddress serverAddress = new SonetServerAddress(ip, port);
        CompletableFuture<Void> future = new CompletableFuture<>();
        try {
            System.out.println(serverAddress.toSocketAddress());
            channel = SocketChannel.open();
            if (channel.connect(new InetSocketAddress(AddressUtils.localAddress, port))) {
                channel.configureBlocking(false);
                future.complete(null);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        invoker = new PacketInvoker() {
            @Override
            @SuppressWarnings("unchecked")
            public void invoke(SonetPacket<?> packet) {
                try {
                    ByteBuffer header = ByteBuffer.allocate(5);
                    SonetBuffer buffer = SonetBuffer.load(header);
                    buffer.writeInt(packet.getData().capacity());
                    Class<? extends SonetPacket<?>> clazz = (Class<? extends SonetPacket<?>>) packet.getClass();
                    byte type = PacketRegistry.getType(clazz);
                    buffer.writeByte(type);
                    channel.write(buffer.toBuffer());
                    channel.write(packet.getData());
                    packet.getData().clear();
                    channel.read(packet.getData());
                    packet.modify(packet.getData());
                    packet.getData().flip();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return future;
    }

    @Override
    public <T extends SonetPacket<T>> void sendPacket(T packet) {
        if (invoker != null) {
            invoker.invoke(packet);
        }
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
    public boolean isValid() {
        return valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
