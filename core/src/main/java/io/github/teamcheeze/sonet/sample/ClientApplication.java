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
import io.github.teamcheeze.sonet.network.data.SonetPacket;
import io.github.teamcheeze.sonet.network.util.AddressUtils;
import java.util.UUID;

public class ClientApplication {
    public static void main(String[] args) {
        Client client = Sonet.createClient();
        SamplePacket.register();
        UUID myId = UUID.randomUUID();
        client.connect(AddressUtils.localAddress, 44444);
        System.out.println("Connected!");
        System.out.println("Old: " + myId);
        System.out.println("Old: " + "OldEntity");
        SonetPacket returned = client.sendPacket(new SamplePacket(myId, "OldEntity"));
        if (returned instanceof SamplePacket samplePacket) {
            System.out.println("New: " + samplePacket.getName());
            System.out.println("New: " + samplePacket.getId());
        }
        client.abort();
    }
}
