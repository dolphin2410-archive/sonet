package io.github.teamcheeze.remoteActions.network.data.sample;

import io.github.teamcheeze.remoteActions.network.Packet;
import org.jetbrains.annotations.NotNull;

public class SampleTwo implements Packet<SampleTwo> {
    @Override
    public @NotNull SampleTwo getData() {
        return this;
    }

    @Override
    public void updateData(Packet<SampleTwo> updated) {

    }
}
