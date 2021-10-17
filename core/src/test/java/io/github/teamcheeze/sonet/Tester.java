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

import io.github.teamcheeze.sonet.network.data.packet.SonetPacket;
import io.github.teamcheeze.sonet.network.handlers.ServerPacketHandler;
import io.github.teamcheeze.sonet.network.util.net.AddressUtils;
import io.github.teamcheeze.sonet.sample.SampleDataContainer;
import io.github.teamcheeze.sonet.sample.SamplePacket;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Tester {
    public static void main(String[] args) {
        SonetServer server = (SonetServer) Sonet.createServer(9090);
        server.addClientHandler(e -> {
            System.out.println("Login Success");
        });
        server.addPacketHandler(new ServerPacketHandler<SamplePacket>() {
            @Override
            public void handle(SamplePacket data) {
                System.out.println("X: " + data.getDataContainer().getX());
            }
        });
        server.startAsync();
        System.out.println("Server Started");
        SonetClient client = (SonetClient) Sonet.createClient();
        client.connect(AddressUtils.localAddress, 9090);
        System.out.println("Connect Success");
        ScheduledExecutorService ex = Executors.newScheduledThreadPool(1);
        AtomicInteger x = new AtomicInteger();
        ex.scheduleAtFixedRate(() -> {
            client.sendPacket(new SamplePacket(UUID.randomUUID(), "NAME", new SampleDataContainer(x.getAndIncrement(), 10)));
        },0, 100, TimeUnit.MILLISECONDS);

    }
}
