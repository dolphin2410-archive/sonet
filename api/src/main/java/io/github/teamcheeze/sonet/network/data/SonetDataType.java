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
    LONG((byte) 0x00, long.class, 8),
    LONG_ARRAY((byte) 0x01, Long[].class, 0),
    INT((byte) 0x02, Integer.class, 4),
    INT_ARRAY((byte) 0x03, Integer[].class, 0),
    BYTE((byte) 0x04, Byte.class, 1),
    BYTE_ARRAY((byte) 0x05, Byte[].class, 0),
    SHORT((byte) 0x06, Short.class, 2),
    SHORT_ARRAY((byte) 0x07, Short[].class, 0),
    FLOAT((byte) 0x08, Float.class, 4),
    FLOAT_ARRAY((byte) 0x09, Float[].class, 0),
    CHAR((byte) 0x0a, Character.class, 2),
    STRING((byte) 0x0b, java.lang.String.class, 0),
    UUID((byte) 0x0c, java.util.UUID.class, 16),
    DOUBLE((byte) 0x0d, Double.class, 8),
    DOUBLES((byte) 0x0e, Double[].class, 0),
    DATA_CONTAINER((byte) 0x0f, SonetDataContainer.class, 0);
    private final byte type;
    private final Class<?> clazz;
    private final int defaultSize;
    SonetDataType(final byte type, final Class<?> clazz, final int defaultSize) {
        this.type = type;
        this.clazz = clazz;
        this.defaultSize = defaultSize;
    }

    public int getDefaultSize() {
        return defaultSize;
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
