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
import io.github.teamcheeze.sonet.network.data.buffer.StaticSonetBuffer;
import java.nio.ByteBuffer;

public class SonetBufferTester {
    public static void main(String[] args) {
        SonetBuffer sonetBuffer = new SonetBuffer();
        sonetBuffer.writeString("io.github.teamcheeze.plum");
        ByteBuffer buf = sonetBuffer.toBuffer();
        StaticSonetBuffer staticSonetBuffer = StaticSonetBuffer.loadReset(buf);
        System.out.println(staticSonetBuffer.readString());
        System.out.println(new String("Hello, World".getBytes()));
    }
}
