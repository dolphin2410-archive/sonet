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

package io.github.teamcheeze.sonet;

import io.github.teamcheeze.sonet.network.data.buffer.SonetBuffer;

import java.nio.ByteBuffer;

public class SonetBufferTester {
    public static void main(String[] args) {
        testTwo();
    }
    public static void testOne() {
        SonetBuffer buffer = new SonetBuffer();
        buffer.writeString("Hello");
        buffer.writeLongs(new long[]{1, 10, 100, 1000, 10000});
        buffer.writeBytes(new byte[]{1, 2, 3, 4, 5});
        buffer.writeInt(100000);
        buffer.writeBoolean(false);
        buffer.writeShort((short) -10);
        buffer.writeChar('H');
        buffer.writeDouble(0.001);
        buffer.writeFloat(1024F);
        buffer.updateBuffer();

        String msg = buffer.readString();
        long[] longs = buffer.readLongs();
        byte[] bytes = buffer.readBytes();
        int j = buffer.readInt();
        boolean bool = buffer.readBoolean();
        short s = buffer.readShort();
        char c = buffer.readChar();
        double d = buffer.readDouble();
        float f = buffer.readFloat();

        Debugger.debug(msg, longs, bytes, j, bool, s, c, d, f);
    }

    public static void testTwo() {
        SonetBuffer buffer = new SonetBuffer();
        buffer.writeString("Hi");
        buffer.writeInt(1359);
        buffer.updateBuffer();
        buffer.readString();
        ByteBuffer b = buffer.cut();
        SonetBuffer newSonetBuffer = SonetBuffer.load(b);
        System.out.println(newSonetBuffer.readInt());
    }
}
