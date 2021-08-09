package io.github.teamcheeze.remoteActions.network;

import io.github.teamcheeze.remoteActions.Main;
import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.util.ExceptionCodes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class IConnectionHandler {
    private final static ArrayList<Client> registeredClients = new ArrayList<>();
    public static void registerClient(Client client) {
        registeredClients.add(client);
    }

    /**
     *
     * @param clientId The id of the client to get the address
     * @return The address of the given client
     */
    @Nullable
    Address getClientAddress(UUID clientId) {
        Stream<Client> filter = registeredClients.stream().filter(it -> it.getId() == clientId);
        if (filter.count() != 1) {
            if (filter.count() > 1) {
                System.exit(ExceptionCodes.DUPLICATE_CLIENT_CODE.getId());
            }
            return null;
        }
        Optional<Client> optionalClient = filter.reduce((first, second)->first);
        if (optionalClient.isEmpty()) {
            return null;
        }
        return optionalClient.get().getAddress();
    }
}
