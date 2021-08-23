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

import io.github.teamcheeze.sonet.network.data.*;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * A packet requires an empty constructor. This constructor is used to create a new instance of a packet after deserialization
 */
public class SamplePacket extends SonetPacket<SamplePacket> {
    private ByteBuffer data;
    private UUID id;
    private String name;

    public SamplePacket(UUID uuid, String name) {
        this.id = uuid;
        this.name = name;
        SonetBuffer sonetBuffer = new SonetBuffer();
        sonetBuffer.writeUUID(uuid);
        sonetBuffer.writeString(name);
        this.data = sonetBuffer.toBuffer();
    }

    @Override
    public ByteBuffer getData() {
        return data;
    }

    @Override
    public void modify(ByteBuffer data) {
        this.data = data;
        SonetBuffer sb = SonetBuffer.load(data);
        this.id = sb.readUUID();
        this.name = sb.readString();
    }

    @SonetDeserialize
    public static SamplePacket deserialize(ByteBuffer buffer) {
        SonetBuffer sonetBuffer = SonetBuffer.load(buffer);
        UUID uuid = sonetBuffer.readUUID();
        String name = sonetBuffer.readString();
        return new SamplePacket(uuid, name);
    }

    @Override
    public ByteBuffer serialize() {
        SonetBuffer sb = new SonetBuffer();
        sb.writeUUID(this.id);
        sb.writeString(this.name);
        return sb.toBuffer();
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }
}
