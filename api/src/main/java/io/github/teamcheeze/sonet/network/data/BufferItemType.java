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

public enum BufferItemType {
    LONG((byte) 0x00, long.class),
    LONG_ARRAY((byte) 0x01, long[].class),
    INT((byte) 0x02, int.class),
    INT_ARRAY((byte) 0x03, int[].class),
    BYTE((byte) 0x04, byte.class),
    BYTE_ARRAY((byte) 0x05, byte[].class),
    SHORT((byte) 0x06, short.class),
    SHORT_ARRAY((byte) 0x07, short[].class),
    FLOAT((byte) 0x08, float.class),
    FLOAT_ARRAY((byte) 0x09, float[].class),
    CHAR((byte) 0x0a, char.class),
    STRING((byte) 0x0b, java.lang.String.class),
    UUID((byte) 0x0c, java.util.UUID.class),
    DOUBLE((byte) 0x0d, double.class),
    DOUBLES((byte) 0x0e, double[].class);
    private final byte type;
    private final Class<?> clazz;
    BufferItemType(final byte type, final Class<?> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public static BufferItemType fromSafe(byte type) {
        for (BufferItemType value : values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        return null;
    }

    public static BufferItemType fromSafe(Class<?> clazz) {
        for (BufferItemType value : values()) {
            if (value.getClazz() == clazz) {
                return value;
            }
        }
        return null;
    }

    public static BufferItemType from(byte type) {
        for (BufferItemType value : values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        throw new RuntimeException("No item found");
    }

    public static BufferItemType from(Class<?> clazz) {
        for (BufferItemType value : values()) {
            if (value.getClazz() == clazz) {
                return value;
            }
        }
        throw new RuntimeException("No item found");
    }

    public byte getType() {
        return type;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
