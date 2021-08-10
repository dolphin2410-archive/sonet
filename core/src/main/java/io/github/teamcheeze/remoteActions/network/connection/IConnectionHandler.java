package io.github.teamcheeze.remoteActions.network.connection;

import io.github.teamcheeze.remoteActions.Main;
import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Address;
import io.github.teamcheeze.remoteActions.server.Server;
import io.github.teamcheeze.remoteActions.util.ExceptionCodes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class IConnectionHandler {
    private final static ArrayList<Client> registeredClients = new ArrayList<>();
    private final static ArrayList<Server> registeredServers = new ArrayList<>();
    public static void registerClient(Client client) {
        registeredClients.add(client);
    }
    public static void registerServer(Server server) {
        registeredServers.add(server);
    }

    /**
     *
     * @param filter Filter
     * @return The address of the given client
     */
    @Nullable
    private static Client getClient(Function<Client, Boolean> filter) {
        Stream<Client> filtered = registeredClients.stream().filter(filter::apply);
        if (filtered.count() != 1) {
            if (filtered.count() > 1) {
                System.exit(ExceptionCodes.DUPLICATE_CLIENT_CODE.getId());
            }
            return null;
        }
        Optional<Client> optionalClient = filtered.reduce((first, second)->first);
        if (optionalClient.isEmpty()) {
            return null;
        }
        return optionalClient.get();
    }

    @Nullable
    private static Server getServer(Function<Server, Boolean> filter) {
        Stream<Server> filtered = registeredServers.stream().filter(filter::apply);
        if (filtered.count() != 1) {
            if (filtered.count() > 1) {
                System.exit(ExceptionCodes.DUPLICATE_CLIENT_CODE.getId());
            }
            return null;
        }
        Optional<Server> optionalClient = filtered.reduce((first, second)->first);
        if (optionalClient.isEmpty()) {
            return null;
        }
        return optionalClient.get();
    }

    @Nullable
    public static Client getClient(Address clientAddress) {
        return getClient((client)->client.getAddress() == clientAddress);
    }

    @Nullable
    public static Client getClient(UUID clientId) {
        return getClient((client)->client.getId() == clientId);
    }

    @Nullable
    public static Server getServer(Address serverAddress) {
        return getServer((server)->server.getAddress() == serverAddress);
    }

    @Nullable
    public static Server getServer(UUID serverId) {
        return getServer((server)->server.getId() == serverId);
    }
}
