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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SerializationObject {
    private final List<Object> objects = new ArrayList<>();
    public SerializationObject() {

    }
    public SerializationObject(Object... objects) {
        this.objects.addAll(Arrays.asList(objects));
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void addObject(Object object) {
        this.objects.add(object);
    }

    public boolean matches(SerializationKey key) {
        if (key.getTypes().size() != getObjects().size())
            return false;
        for (int i = 0; i < key.getTypes().size(); i++) {
            if (!(key.getTypes().get(i).getClazz().equals(getObjects().get(i).getClass()))) {
                return false;
            }
        }
        return true;
    }
}
