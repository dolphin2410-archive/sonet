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

package io.github.teamcheeze.sonet.network.handlers;

import io.github.teamcheeze.sonet.network.data.packet.SonetPacket;

public abstract class ServerPacketHandler<T extends SonetPacket> implements SonetPacketHandler<T> {
    public boolean packetSent = false;
    private SonetPacket packet = null;
    protected void send(SonetPacket packet) {
        if (!packetSent) {
            packetSent = true;
            this.packet = packet;
        }
    }
    private SonetPacket getPacket() {
        return packet;
    }
}
