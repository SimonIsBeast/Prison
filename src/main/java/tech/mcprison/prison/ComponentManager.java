/*
 * Prison - A plugin for the Minecraft Bukkit mod
 * Copyright (C) 2016  SirFaizdat
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
 *
 */

package tech.mcprison.prison;

import tech.mcprison.prison.core.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages instances of all components, and enables/disables them.
 *
 * @author SirFaizdat
 * @since 2.3
 */
public class ComponentManager {

    public List<Component> components;

    public ComponentManager() {
        this.components = new ArrayList<>();
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void enableAll() {
        for (Component component : components) {
            if (!component.isEnabled())
                continue; // Skip it

            if (!component.enable()) {
                Prison.l.severe("Component " + component.getName() + " failed to load.");
                component.setEnabled(false);
            } else {
                component.setEnabled(true);
                Prison.l.info("Component " + component.getName() + " loaded.");
            }
        }
    }

    public void disableAll() {
        for (Component component : components) {
            component.disable();
            Prison.l.info("Component " + component.getName() + " unloaded.");
        }
    }

    public Component getComponent(String componentName) {
        for (Component component : components)
            if (component.getName().equalsIgnoreCase(componentName))
                return component;
        return null;
    }

}
