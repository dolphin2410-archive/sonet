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

package io.github.teamcheeze.sonet;

import io.github.teamcheeze.sonet.network.component.Client;
import io.github.teamcheeze.sonet.network.component.Server;
import io.github.teamcheeze.sonet.network.util.loader.SonetFactory;

public class ISonetFactory implements SonetFactory {
    @Override
    public Client createClient() {
        return new SonetClient();
    }

    @Override
    public Server createServer(int port) {
        return new SonetServer(port);
    }
}
