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

package io.github.teamcheeze.sonet.network;

import io.github.teamcheeze.sonet.network.data.SonetPacket;
import io.github.teamcheeze.sonet.sample.SamplePacket;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PacketRegistry {
    public static Map<Byte, Class<? extends SonetPacket<?>>> PACKET_REGISTRY = new HashMap<>();
    static {
        PACKET_REGISTRY.put((byte) 0x00, SamplePacket.class);
    }

    public static void register(byte id, SonetPacket<?> packet) {
        if (PACKET_REGISTRY.containsKey(id)) {
            throw new RuntimeException("The id already exists");
        }
    }

    public static byte getType(Class<? extends SonetPacket<?>> packetClass) {
        Iterator<Map.Entry<Byte, Class<? extends SonetPacket<?>>>> iterator = PACKET_REGISTRY.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Byte, Class<? extends SonetPacket<?>>> entry = iterator.next();
            if (entry.getKey() == null) {
                iterator.remove();
            }
            if (entry.getValue() == packetClass) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Unregistered packet");
    }
    public static Class<? extends SonetPacket<?>> getClass(byte type) {
        Class<? extends SonetPacket<?>> clazz = PACKET_REGISTRY.get(type);
        if (clazz == null)
            throw new RuntimeException("Unregistered packet");
        else
            return clazz;
    }
}
