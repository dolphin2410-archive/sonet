package io.github.teamcheeze.remoteActions.network.data.sample;

import io.github.teamcheeze.remoteActions.network.Packet;
import org.jetbrains.annotations.NotNull;

public class SampleOne implements Packet<SampleOne> {
    @Override
    public @NotNull SampleOne getData() {
        return this;
    }

    @Override
    public void updateData(Packet<SampleOne> updated) {

    }
}
