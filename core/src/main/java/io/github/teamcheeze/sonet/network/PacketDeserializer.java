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

import io.github.teamcheeze.sonet.annotations.SonetDeserialize;
import io.github.teamcheeze.sonet.network.data.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class PacketDeserializer {
    public static SonetPacket deserialize(byte packetType, ByteBuffer body) throws PacketNotFoundException {
        Class<? extends SonetPacket> clazz = PacketRegistry.PACKET_REGISTRY.get(packetType);
        for (Method method : clazz.getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotationsByType(SonetDeserialize.class);
            if (annotations.length == 0)
                continue;
            try {
                return (SonetPacket) method.invoke(null, body);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("The packet needs a static method '@SonetDeserialize [methodName](ByteBuffer)'");
    }
}
