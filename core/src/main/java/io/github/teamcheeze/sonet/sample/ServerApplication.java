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

package io.github.teamcheeze.sonet.sample;

import io.github.teamcheeze.sonet.Sonet;
import io.github.teamcheeze.sonet.SonetServer;
import io.github.teamcheeze.sonet.network.component.Server;
import io.github.teamcheeze.sonet.network.handlers.ServerPacketHandler;
import io.github.teamcheeze.sonet.network.handlers.SonetConnectionHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public class ServerApplication {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        Server server = Sonet.createServer(9090);
        System.out.println("Server Created");
        SonetConnectionHandler clientHandler = clientChannel -> {
            try {
                System.out.println("User with IP: " + ((InetSocketAddress) clientChannel.getRemoteAddress()).getAddress().getHostAddress() + " successfully connected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        server.addClientHandler(clientHandler);
        ServerPacketHandler<SamplePacket> packetHandler = new ServerPacketHandler<>() {
            @Override
            public void handle(SamplePacket packet) {
                packet.setName("");
            }
        };
        server.addPacketHandler(packetHandler);
        server.startAsync();
    }
}