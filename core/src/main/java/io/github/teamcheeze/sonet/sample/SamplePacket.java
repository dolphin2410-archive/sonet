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
import java.util.Objects;
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

    public SamplePacket() {

    }

    @Override
    public ByteBuffer getData() {
        return data;
    }

    @Override
    public void setData(ByteBuffer data) {
        this.data = data;
        SonetBuffer sb = SonetBuffer.load(data);
        this.id = sb.readUUID();
        this.name = sb.readString();
    }

    @Override
    public SamplePacket deserialize(ByteBuffer buffer) {
        SonetBuffer sonetBuffer = SonetBuffer.load(buffer);
        UUID uuid = sonetBuffer.readUUID();
        String name = sonetBuffer.readString();
        return new SamplePacket(uuid, name);
    }

    @Override
    public ByteBuffer serialize() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @SonetDeserialize
    public static <T extends SonetPacket<?>> T fromObjects(Class<T> clazz, SerializationObject obj) {
        if (clazz != SonetPacket.class) {
            throw new RuntimeException("Class mismatch");
        }
        SonetBuffer buffer = new SonetBuffer();
        if (obj.matches(key)) {
            for (int i = 0; i < key.getTypes().size(); i++) {
                buffer.write(key.getTypes().get(i).getType(), obj.getObjects().get(i));
            }
        } else {
            throw new RuntimeException("Serialization object doesn't match the serialization key");
        }
        buffer.updateBuffer();
        return (T) new SamplePacket(buffer.readUUID(), buffer.readString());
    }

    public static SerializationKey key = new SerializationKey(SerializationKeyType.UUID, SerializationKeyType.STRING);

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SamplePacket that = (SamplePacket) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
