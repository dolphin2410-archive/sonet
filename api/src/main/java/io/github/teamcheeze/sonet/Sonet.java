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
import io.github.teamcheeze.sonet.network.util.CoreLoader;
import io.github.teamcheeze.sonet.network.util.SonetFactory;

public class Sonet {
    private static SonetFactory factory = null;

    static {
        setFactory(CoreLoader.loadFactory());
    }

    public static void setFactory(SonetFactory factory) {
        if (Sonet.factory != null) {
            throw new RuntimeException("Cannot reinitialize a factory");
        }
        Sonet.factory = factory;
    }

    public static SonetFactory getFactory() {
        return factory;
    }

    public static Client createClient() {
        return factory.createClient();
    }

    public static Server createServer(int port) {
        return factory.createServer(port);
    }
}
