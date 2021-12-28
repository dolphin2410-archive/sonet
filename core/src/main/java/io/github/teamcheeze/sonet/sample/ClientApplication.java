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
import io.github.teamcheeze.sonet.network.component.Client;
import io.github.teamcheeze.sonet.network.component.Server;
import io.github.teamcheeze.sonet.network.data.packet.SonetPacket;
import io.github.teamcheeze.sonet.network.handlers.ClientPacketHandler;
import io.github.teamcheeze.sonet.network.util.net.AddressUtils;

import java.util.UUID;

public class ClientApplication {
    public static void main(String[] args) throws InterruptedException {
        Client client = Sonet.createClient();
        client.connect(AddressUtils.localAddress, 9090);
        System.out.println("Connected!");
        SamplePacket packet = new SamplePacket(UUID.randomUUID(), "OldEntity", new SampleDataContainer(10, 215));
        System.out.println("Old: " + packet.getId());
        System.out.println("Old: " + packet.getName());
        System.out.println("OldX: " + packet.getDataContainer().getX());
        System.out.println("OldY: " + packet.getDataContainer().getY());
        client.addPacketHandler(new ClientPacketHandler<SamplePacket>() {
            @Override
            public void handle(SamplePacket data) {
                System.out.println("New: " + data.getName());
                System.out.println("New: " + data.getId());
                System.out.println("NewX: " + data.getDataContainer().getX());
                System.out.println("NewY: " + data.getDataContainer().getY());
            }
        });
        client.sendPacket(packet);
//        client.abort();
//        thread.join();

    }
}
