/*
 * Prison is a Minecraft plugin for the prison gamemode.
 * Copyright (C) 2016 SirFaizdat
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

package io.github.sirfaizdat.prison;

/**
 * Represents Prison's configuration.
 * Contains all of the keys and their default values.
 * Every time a new value is added to a production version (i.e. public release),
 * the VERSION constant must be incremented by one to ensure the configuration will be regenerated.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class Configuration {

    public static final int VERSION = 0; // For everyone to reference
    public int version = VERSION; // For the configuration file to store

    // Entries

    public String defaultResetMethod = "test";
    public String defaultTrigger = "time";
    public double defaultResetInterval = 300;

}
