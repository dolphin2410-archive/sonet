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

import io.github.dolphin2410.jaw.reflection.MethodAccessor;
import io.github.teamcheeze.sonet.network.data.*;
import io.github.teamcheeze.sonet.sample.SamplePacket;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PacketDeserializer {
    public static SonetPacket<?> deserialize(byte packetType, ByteBuffer body) throws PacketNotFoundException {
        Class<? extends SonetPacket<?>> clazz = PacketRegistry.PACKET_REGISTRY.get(packetType);
        Method[] methods = SamplePacket.class.getDeclaredMethods();
        Stream<Method> filtered = Arrays.stream(methods).filter(it -> it.getName().equals("deserialize") && it.getParameterCount() == 1 && it.getParameterTypes()[0] == ByteBuffer.class && it.getAnnotationsByType(SonetDeserialize.class).length == 1 && Modifier.isStatic(it.getModifiers()));
        List<Method> filteredMethods = filtered.toList();
        if (filteredMethods.size() == 1) {
            MethodAccessor<? extends SonetPacket<?>> accessor = new MethodAccessor<>(clazz, "deserialize");
            return (SonetPacket<?>) accessor.invoke(body);
        }
        throw new RuntimeException("The packet needs a static 'deserialize(ByteBuffer)' method annotated with @SonetDeserialize");
    }
}
