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

package io.github.teamcheeze.sonet.network.data.packet;

import io.github.teamcheeze.sonet.annotations.SonetConstruct;
import io.github.teamcheeze.sonet.annotations.SonetData;
import io.github.teamcheeze.sonet.annotations.SonetDeserialize;
import io.github.teamcheeze.sonet.network.data.SonetDataType;
import io.github.teamcheeze.sonet.network.data.buffer.SonetBuffer;
import io.github.teamcheeze.sonet.network.util.CastManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class SonetDataDeserializer {
    private static AbstractSonetData handleSonetConstruct(Class<? extends AbstractSonetData> clazz, ByteBuffer data) {
        SonetBuffer sb = SonetBuffer.loadReset(data);
        for (Constructor<?> declaredConstructor : clazz.getDeclaredConstructors()) {
            SonetConstruct[] annotations = declaredConstructor.getAnnotationsByType(SonetConstruct.class);
            if (annotations.length == 0)
                continue;
            Object[] parameterValues = new Object[declaredConstructor.getParameterCount()];
            int i = 0;
            for (Class<?> parameterType : declaredConstructor.getParameterTypes()) {
                parameterValues[i++] = sb.read(SonetDataType.from(parameterType));
            }
            try {
                return (AbstractSonetData) declaredConstructor.newInstance(parameterValues);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static AbstractSonetData deserializeSonetData(ByteBuffer body) {
        SonetBuffer sb = SonetBuffer.loadReset(body);
        String className = sb.readString();
        Class<? extends AbstractSonetData> clazz;
        try {
            clazz = (Class<? extends AbstractSonetData>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            throw new RuntimeException("Not Found");
        }
        ByteBuffer rawData = sb.cut();
        AbstractSonetData data = handleSonetConstruct(clazz, rawData);
        if (data != null) {
            return data;
        }
        for (Method method : clazz.getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotationsByType(SonetDeserialize.class);
            if (annotations.length == 0)
                continue;
            try {
                return CastManager.safeCast(method.invoke(null, rawData), AbstractSonetData.class);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("The packet needs a static method '@SonetDeserialize [methodName](ByteBuffer)'");
    }

    public static SonetPacket deserializePacket(ByteBuffer body) throws PacketNotFoundException {
        return deserializeSonetData(body).asPacket();
    }

    public static SonetDataContainer deserializeContainer(ByteBuffer body) {
        return deserializeSonetData(body).asContainer();
    }
}