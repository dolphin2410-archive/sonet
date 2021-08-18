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
import io.github.teamcheeze.sonet.network.util.AddressUtils;

import java.util.UUID;

public class ClientApplication {
    public static void main(String[] args) {
        Client client = Sonet.createClient();
        client.connect(AddressUtils.localAddress, 44444).thenRun(()->{
            System.out.println("Connected");
            client.sendPacket(new SamplePacket(UUID.randomUUID(), "mr2"));
            client.abort();
        });
    }
}