/**
 *    Copyright 2015-2020 hexosse <hexosse@gmail.com>
 *
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.hexocraft.configuration;

import com.github.hexocraft.configuration.xml.XmlConfiguration;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.awt.*;
import java.io.File;

@ConfigSerializable
public class XmlBaseConfig extends XmlConfiguration<XmlBaseConfig> {

    @Comment("Config version")
    public int version = 1;

    @Comment("Database")
    public Database database;

    @Comment("RGB Color")
    public Color color = new Color(13, 32, 64);

    @Comment("ARGB Color")
    public Color aplhaColor = new Color(13, 32, 64, 128);

    public XmlBaseConfig(@NonNull File file) throws ConfigurateException {
        super(TypeToken.get(XmlBaseConfig.class), file);
    }

    public XmlBaseConfig() throws ConfigurateException {
        super(TypeToken.get(XmlBaseConfig.class));
    }

    public Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    @ConfigSerializable
    static class Database {
        @Comment("Set the database to use")
        public String host = "localhost";
        @Comment("Configure database port")
        public int port = 3306;
    }
}