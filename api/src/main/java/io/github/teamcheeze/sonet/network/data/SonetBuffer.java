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

import io.github.dolphin2410.jaw.util.collection.Pair;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.UUID;

public final class SonetBuffer {
    private ByteBuffer buffer;
    private final ByteArrayOutputStream byteArrayOutputStream;
    private final DataOutputStream dataOutputStream;

    public SonetBuffer() {
        this(new byte[0]);
    }

    public SonetBuffer(byte[] data) {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(data);
        } catch (IOException e) {
            System.out.println("Failed to write data.");
            e.printStackTrace();
        }
        this.dataOutputStream = new DataOutputStream(this.byteArrayOutputStream);
        this.buffer = toBuffer();
        this.buffer.position(0);
    }

    public static SonetBuffer load(final ByteBuffer buffer) {
        return new SonetBuffer(buffer.array());
    }

    public void updateBuffer() {
        buffer = toBuffer();
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public byte[] readRaw() {
        return byteArrayOutputStream.toByteArray();
    }

    public void writeRaw(final byte[] data) {
        try {
            byteArrayOutputStream.write(data);
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeItem(final SonetBufferItem item) {
        writeBytes(item.getTypesArray());
        for (Pair<BufferItemType, Object> pair : item.items) {
            write(pair.getFirst().getType(), pair);
        }
    }

    public SonetBufferItem readItem() {
        SonetBufferItem item = new SonetBufferItem();
        byte[] types = readBytes();
        for (byte type : types) {
            Object obj = read(type);
            item.set(BufferItemType.from(type), obj);
        }
        return item;
    }

    public Object read(final byte type) {
        return switch (type) {
            case 0x00 -> readLong();
            case 0x01 -> readLongs();
            case 0x02 -> readInt();
            case 0x03 -> readInts();
            case 0x04 -> readByte();
            case 0x05 -> readBytes();
            case 0x06 -> readShort();
            case 0x07 -> readShorts();
            case 0x08 -> readFloat();
            case 0x09 -> readFloats();
            case 0x0a -> readChar();
            case 0x0b -> readString();
            case 0x0c -> readUUID();
            case 0x0d -> readDouble();
            case 0x0e -> readDoubles();
            default -> throw new RuntimeException("Invalid type");
        };
    }

    private void write(Object object) {
        write(BufferItemType.from(object.getClass()).getType(), object);
    }

    public void write(final byte type, final Object object) {
        switch (type) {
            case -0x01 -> write(object);
            case 0x00 -> writeLong((long) object);
            case 0x01 -> writeLongs((long[]) object);
            case 0x02 -> writeInt((int) object);
            case 0x03 -> writeInts((int[]) object);
            case 0x04 -> writeByte((byte) object);
            case 0x05 -> writeBytes((byte[]) object);
            case 0x06 -> writeShort((short) object);
            case 0x07 -> writeShorts((short[]) object);
            case 0x08 -> writeFloat((float) object);
            case 0x09 -> writeFloats((float[]) object);
            case 0x0a -> writeChar((char) object);
            case 0x0b -> writeString((String) object);
            case 0x0c -> writeUUID((UUID) object);
            case 0x0d -> writeDouble((double) object);
            case 0x0e -> writeDoubles((double[]) object);
            default -> throw new RuntimeException("Invalid type");
        }
    }

    public String readString() {
        int size = readInt();
        char[] chars = new char[size];
        for (int i = 0; i < size; i++) {
            chars[i] = readChar();
        }
        return new String(chars);
    }

    public char readChar() {
        return buffer.getChar();
    }

    public void writeChar(final char c) {
        try {
            dataOutputStream.writeChar(c);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public short readShort() {
        return buffer.getShort();
    }

    public void writeShort(final short s) {
        try {
            dataOutputStream.writeShort(s);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public short[] readShorts() {
        int size = readInt();
        short[] shorts = new short[size];
        for (int i = 0; i < size; i++) {
            shorts[i] = readShort();
        }
        return shorts;
    }

    public void writeShorts(final short[] shorts) {
        writeInt(shorts.length);
        for (short aShort : shorts) {
            writeShort(aShort);
        }
    }

    public float readFloat() {
        return buffer.getFloat();
    }

    public void writeFloat(final float f) {
        try {
            dataOutputStream.writeFloat(f);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float[] readFloats() {
        int size = readInt();
        float[] floats = new float[size];
        for (int i = 0; i < size; i++) {
            floats[i] = readFloat();
        }
        return floats;
    }

    public void writeFloats(final float[] floats) {
        writeInt(floats.length);
        for (float aFloat : floats) {
            writeFloat(aFloat);
        }
    }

    public void writeString(final String string) {
        writeInt(string.length());
        for (char c : string.toCharArray()) {
            writeChar(c);
        }
    }

    public UUID readUUID() {
        long most = readLong();
        long least = readLong();
        return new UUID(most, least);
    }

    public void writeUUID(final UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public int readInt() {
        return buffer.getInt();
    }

    public void writeInt(final int i) {
        try {
            dataOutputStream.writeInt(i);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[] readInts() {
        int size = readInt();
        int[] ints = new int[size];
        for (int i = 0; i < size; i++) {
            ints[i] = readInt();
        }
        return ints;
    }

    public void writeInts(final int[] ints) {
        writeInt(ints.length);
        for (int anInt : ints) {
            writeInt(anInt);
        }
    }

    public long readLong() {
        long l = buffer.getLong();
        return l;
    }

    public void writeLong(final long l) {
        try {
            dataOutputStream.writeLong(l);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double readDouble() {
        return buffer.getDouble();
    }

    public void writeDouble(final double d) {
        try {
            dataOutputStream.writeDouble(d);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDoubles(final double[] doubles) {
        writeInt(doubles.length);
        for (double aDouble : doubles) {
            writeDouble(aDouble);
        }
    }

    public double[] readDoubles() {
        int size = readInt();
        double[] doubles = new double[size];
        for (int i = 0; i < size; i++) {
            doubles[i] = readDouble();
        }
        return doubles;
    }

    public long[] readLongs() {
        int size = readInt();
        long[] longs = new long[size];
        for (int i = 0; i < size; i++) {
            longs[i] = readLong();
        }
        return longs;
    }

    public void writeLongs(final long[] longs) {
        writeInt(longs.length);
        for (long aLong : longs) {
            writeLong(aLong);
        }
    }

    public void writeByte(final byte b) {
        try {
            dataOutputStream.writeByte(b);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte readByte() {
        return buffer.get();
    }

    public void writeBytes(final byte[] bytes) {
        writeInt(bytes.length);
        for (byte aByte : bytes) {
            writeByte(aByte);
        }
    }

    public byte[] readBytes() {
        int size = readInt();
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = readByte();
        }
        return bytes;
    }

    public ByteBuffer toBuffer() {
        return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
    }
}
