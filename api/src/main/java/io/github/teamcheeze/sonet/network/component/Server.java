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

package io.github.teamcheeze.sonet.network.component;

import io.github.teamcheeze.sonet.network.handlers.SonetConnectionHandler;
import io.github.teamcheeze.sonet.network.handlers.SonetPacketHandler;
import io.github.teamcheeze.sonet.network.util.SonetServerAddress;

import java.util.List;
import java.util.UUID;

public interface Server {
    boolean isValid();
    void setValid(boolean valid);
    UUID getId();
    SonetServerAddress getAddress();
    void start();
    void addClientHandler(SonetConnectionHandler handler);
    void addPacketHandler(SonetPacketHandler handler);
    void removeClientHandler(SonetConnectionHandler handler);
    void removePacketHandler(SonetPacketHandler handler);
    List<SonetConnectionHandler> getClientHandlers();
    List<SonetPacketHandler> getPacketHandlers();
}
