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
import io.github.teamcheeze.sonet.network.data.packet.SonetPacket;
import io.github.teamcheeze.sonet.network.util.net.AddressUtils;

import java.util.UUID;

public class ClientApplication {
    public static void main(String[] args) {
        Client client = Sonet.createClient();
        client.connect(AddressUtils.localAddress, 44444);
        System.out.println("Connected!");
        SamplePacket packet = new SamplePacket(UUID.randomUUID(), "OldEntity", new SampleDataContainer(10, 215));
        System.out.println("Old: " + packet.getId());
        System.out.println("Old: " + packet.getName());
        System.out.println("OldX: " + packet.getDataContainer().getX());
        System.out.println("OldY: " + packet.getDataContainer().getY());
        SonetPacket returned = client.sendPacket(packet);
        if (returned instanceof SamplePacket samplePacket) {
            System.out.println("New: " + samplePacket.getName());
            System.out.println("New: " + samplePacket.getId());
            System.out.println("NewX: " + samplePacket.getDataContainer().getX());
            System.out.println("NewY: " + samplePacket.getDataContainer().getY());
        }
        client.abort();
    }
}
