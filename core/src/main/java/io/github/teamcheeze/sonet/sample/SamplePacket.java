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

import io.github.teamcheeze.sonet.annotations.SonetData;
import io.github.teamcheeze.sonet.annotations.SonetDeserialize;
import io.github.teamcheeze.sonet.network.data.buffer.SonetBuffer;
import io.github.teamcheeze.sonet.network.data.packet.SonetPacket;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * A packet requires an empty constructor. This constructor is used to create a new instance of a packet after deserialization
 */
public class SamplePacket implements SonetPacket {
    @SonetData
    private UUID id;

    @SonetData
    private String name;

    @SonetData
    private SampleDataContainer dataContainer;

    public SamplePacket(UUID uuid, String name, SampleDataContainer dataContainer) {
        this.id = uuid;
        this.name = name;
        this.dataContainer = dataContainer;
    }

    @SonetDeserialize
    public static SamplePacket deserialize(ByteBuffer buffer) {
        SonetBuffer sonetBuffer = SonetBuffer.load(buffer);
        sonetBuffer.updateBuffer();
        UUID uuid = sonetBuffer.readUUID();
        String name = sonetBuffer.readString();
        SampleDataContainer dataContainer = sonetBuffer.readContainer(SampleDataContainer.class);
        return new SamplePacket(uuid, name, dataContainer);
    }

    public String getName() {
        return name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public SampleDataContainer getDataContainer() {
        return dataContainer;
    }

    public void setDataContainer(SampleDataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }
}
