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

import io.github.dolphin2410.jaw.reflection.FieldAccessor;
import io.github.dolphin2410.jaw.reflection.MethodAccessor;
import io.github.teamcheeze.sonet.network.data.*;
import java.nio.ByteBuffer;

public class PacketDeserializer {
    public static SonetPacket<?> deserialize(byte packetType, ByteBuffer body) throws PacketNotFoundException {
        Class<? extends SonetPacket<?>> clazz = PacketRegistry.PACKET_REGISTRY.get(packetType);
        SonetBuffer buffer = SonetBuffer.load(body);
        if (clazz == null) {
            throw new PacketNotFoundException(packetType);
        }
        SerializationKey key = (SerializationKey) new FieldAccessor<>(clazz, "field").get();
        if (key == null) {
            throw new RuntimeException("No serialization key set for packet. Invalid packet.");
        }
        SerializationObject object = new SerializationObject();
        for (SerializationKeyType type : key.getTypes()) {
            object.addObject(buffer.read(type.getType()));
        }
        MethodAccessor<? extends SonetPacket<?>> accessor = new MethodAccessor<>(clazz, "fromObjects");
        return (SonetPacket<?>) accessor.invoke(clazz, object);
    }
}
