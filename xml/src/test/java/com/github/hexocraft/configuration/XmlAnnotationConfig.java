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

import com.github.hexocraft.configuration.annotations.Configuration;
import com.github.hexocraft.configuration.xml.XmlConfiguration;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@Configuration(
        clazz = XmlAnnotationConfig.class,
        file = "../target/tmp/config.xml",
        header = "########################################################\n" +
                "\n" +
                "XML Config file\n" +
                "\n" +
                "########################################################"
)
@ConfigSerializable
public class XmlAnnotationConfig extends XmlConfiguration<XmlAnnotationConfig> {

    @Comment("Config version")
    public int version = 1;

    @Comment("Database")
    public Database database;

    public XmlAnnotationConfig() throws ConfigurateException {
        super(null);
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