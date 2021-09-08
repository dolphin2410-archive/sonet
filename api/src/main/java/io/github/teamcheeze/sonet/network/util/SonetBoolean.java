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

package io.github.teamcheeze.sonet.network.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SonetBoolean {
    private boolean internal;
    private final List<Runnable> executions = new ArrayList<>();
    private final List<Runnable> onceExec = new ArrayList<>();

    public SonetBoolean(boolean init) {
        this.internal = init;
    }

    public boolean get() {
        return internal;
    }

    public void set(boolean value) {
        if (value && !internal) {
            for (Runnable execution : executions) {
                execution.run();
            }
            Iterator<Runnable> runnableIterator = onceExec.iterator();
            while (runnableIterator.hasNext()) {
                runnableIterator.next().run();
                runnableIterator.remove();
            }
            set(false);
            return;
        }
        internal = value;
    }

    public void triggerOnTrue(Runnable execution) {
        executions.add(execution);
    }

    public void triggerOnceOnTrue(Runnable execution) {
        onceExec.add(execution);
    }
}
