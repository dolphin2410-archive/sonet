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

package io.github.teamcheeze.sonet.network.data.buffer;

import io.github.teamcheeze.jaw.reflection.MethodAccessor;
import io.github.teamcheeze.jaw.reflection.ReflectionException;
import io.github.teamcheeze.sonet.network.data.SonetDataType;
import io.github.teamcheeze.sonet.network.data.packet.SonetDataContainer;
import io.github.teamcheeze.sonet.network.data.packet.SonetDataDeserializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class StaticSonetBuffer {
    private ByteBuffer buffer;

    public StaticSonetBuffer(int capacity) {
        this(capacity, new byte[0]);
    }

    public StaticSonetBuffer(int capacity, byte[] data) {
        this.buffer = ByteBuffer.allocate(capacity);
        this.buffer.put(data);
    }

    public StaticSonetBuffer(ByteBuffer byteBuffer) {
        this(byteBuffer.capacity(), byteBuffer.array());
        this.buffer.position(byteBuffer.position());
    }

    public void destroy() {
        buffer.clear();
        buffer = null;
    }

    public static StaticSonetBuffer load(final ByteBuffer buffer) {
        return new StaticSonetBuffer(buffer);
    }

    public static StaticSonetBuffer loadReset(final ByteBuffer buffer) {
        buffer.position(0);
        return load(buffer);
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public byte[] readRaw() {
        return buffer.array();
    }

    public void writeRaw(final byte[] data) {
        this.buffer.put(data);
    }

    public void writeContainer(SonetDataContainer container) {
        try {
            writeBytes((container.serialize()).array());
        } catch (ReflectionException e) {
            e.raw.printStackTrace();
        }
    }

    public SonetDataContainer readContainer() {
        return SonetDataDeserializer.deserializeContainer(ByteBuffer.wrap(readBytes()));
    }

    public <T extends SonetDataContainer> T readContainer(Class<T> clazz) {
        return clazz.cast(readContainer());
    }

    public Object read(final SonetDataType type) {
        return read(type.getType());
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
            case 0x0f -> readContainer();
            default -> throw new RuntimeException("Invalid type");
        };
    }

    private void write(Object object) {
        write(SonetDataType.from(object.getClass()).getType(), object);
    }

    public void write(final SonetDataType type, final Object object) {
        write(type.getType(), object);
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
            case 0x0f -> writeContainer((SonetDataContainer) object);
            default -> throw new RuntimeException("Invalid type");
        }
    }

    public String readString() {
        int size = buffer.getInt();
        byte[] bytes = new byte[size];
        buffer.get(bytes);
        return new String(bytes);
    }

    public char readChar() {
        return buffer.getChar();
    }

    public void writeChar(final char c) {
        this.buffer.putChar(c);
    }

    public short readShort() {
        return buffer.getShort();
    }

    public void writeShort(final short s) {
        buffer.putShort(s);
    }

    public boolean readBoolean() {
        return buffer.get() == 1;
    }

    public void writeBoolean(final boolean b) {
        buffer.put((byte) (b ? 1 : 0));
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
        this.buffer.putFloat(f);
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
        writeInt(string.getBytes().length);
        writeBytes(string.getBytes());
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
        this.buffer.putInt(i);
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
        return buffer.getLong();
    }

    public void writeLong(final long l) {
        this.buffer.putLong(l);
    }

    public double readDouble() {
        return buffer.getDouble();
    }

    public void writeDouble(final double d) {
        this.buffer.putDouble(d);
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
        this.buffer.put(b);
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

    public ByteBuffer cut() {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return ByteBuffer.wrap(bytes);
    }
}
