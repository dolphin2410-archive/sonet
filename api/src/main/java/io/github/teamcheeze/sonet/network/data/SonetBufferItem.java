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

package io.github.teamcheeze.sonet.network.data;

import io.github.dolphin2410.jaw.util.collection.Pair;
import java.util.ArrayList;
import java.util.List;

public class SonetBufferItem {
    List<Pair<BufferItemType, Object>> items = new ArrayList<>();
    public SonetBufferItem() {
    }
    public int getLength() {
        return items.size();
    }

    public byte[] getTypesArray() {
        byte[] types = new byte[getLength()];
        for (int i = 0; i < getLength(); i++) {
            types[i] = items.get(i).getFirst().getType();
        }
        return types;
    }

    public void set(BufferItemType type, Object item) {
        if (type.getClazz().isInstance(item))
            items.add(Pair.of(type, item));
    }
}
