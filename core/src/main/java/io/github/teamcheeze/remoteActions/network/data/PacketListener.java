package io.github.teamcheeze.remoteActions.network.data;

import io.github.dolphin2410.jaw.util.kotlin.KWrapper;

import java.util.ArrayList;

public abstract class PacketListener {
    private static final ArrayList<PacketListener> listeners = new ArrayList<>();
    public static PacketListener addListener(PacketListener listener) {
        return KWrapper.apply(listener, listeners::add);
    }

    public static void removeListener(PacketListener listener) {
        listeners.remove(listener);
    }

    public static ArrayList<PacketListener> getListeners() {
        return listeners;
    }

    public void onReceived(Object object) {
    }
}
