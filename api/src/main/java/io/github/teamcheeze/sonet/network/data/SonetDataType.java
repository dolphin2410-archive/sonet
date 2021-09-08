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

package io.github.teamcheeze.sonet.network.data;

import io.github.teamcheeze.sonet.network.data.packet.SonetDataContainer;

public enum SonetDataType {
    LONG((byte) 0x00, long.class),
    LONG_ARRAY((byte) 0x01, Long[].class),
    INT((byte) 0x02, Integer.class),
    INT_ARRAY((byte) 0x03, Integer[].class),
    BYTE((byte) 0x04, Byte.class),
    BYTE_ARRAY((byte) 0x05, Byte[].class),
    SHORT((byte) 0x06, Short.class),
    SHORT_ARRAY((byte) 0x07, Short[].class),
    FLOAT((byte) 0x08, Float.class),
    FLOAT_ARRAY((byte) 0x09, Float[].class),
    CHAR((byte) 0x0a, Character.class),
    STRING((byte) 0x0b, java.lang.String.class),
    UUID((byte) 0x0c, java.util.UUID.class),
    DOUBLE((byte) 0x0d, Double.class),
    DOUBLES((byte) 0x0e, Double[].class),
    DATA_CONTAINER((byte) 0x0f, SonetDataContainer.class);
    private final byte type;
    private final Class<?> clazz;
    SonetDataType(final byte type, final Class<?> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public static SonetDataType fromSafe(byte type) {
        for (SonetDataType value : values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        return null;
    }

    public static SonetDataType fromSafe(Class<?> clazz) {
        for (SonetDataType value : values()) {
            if (value.getClazz() == clazz) {
                return value;
            }
        }
        return null;
    }

    public static SonetDataType from(byte type) {
        for (SonetDataType value : values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        throw new RuntimeException("No item found");
    }

    public static SonetDataType from(Class<?> clazz) {
        for (SonetDataType value : values()) {
            if (value.getClazz() == clazz) {
                return value;
            }
        }
        if (SonetDataContainer.class.isAssignableFrom(clazz)) {
            return SonetDataType.DATA_CONTAINER;
        } else {
            System.out.println("NOT " + clazz);
        }
        throw new RuntimeException("No item found");
    }

    public static boolean hasType(Object datum) {
        for (SonetDataType value : values()) {
            if (value.getClazz() == datum.getClass())
                return true;
        }
        return false;
    }

    public byte getType() {
        return type;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
