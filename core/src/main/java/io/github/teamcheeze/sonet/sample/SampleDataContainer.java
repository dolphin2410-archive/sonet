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

package io.github.teamcheeze.sonet.sample;

import io.github.teamcheeze.sonet.annotations.SonetConstruct;
import io.github.teamcheeze.sonet.annotations.SonetData;
import io.github.teamcheeze.sonet.annotations.SonetDeserialize;
import io.github.teamcheeze.sonet.network.data.buffer.SonetBuffer;
import io.github.teamcheeze.sonet.network.data.buffer.StaticSonetBuffer;
import io.github.teamcheeze.sonet.network.data.packet.SonetDataContainer;

import java.nio.ByteBuffer;

public class SampleDataContainer implements SonetDataContainer {
    @SonetData
    private int x;

    @SonetData
    private int y;

    @SonetConstruct
    public SampleDataContainer(int x, int y) {
        this.x = x;
        this.y = y;
    }

//    @SonetDeserialize
//    public static SampleDataContainer deserialize(ByteBuffer buffer) {
//        StaticSonetBuffer sb = StaticSonetBuffer.loadReset(buffer);
//        return new SampleDataContainer(sb.readInt(), sb.readInt());
//    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
