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
package com.github.hexocraft.configurate;

import com.github.hexocraft.configurate.annotations.Configuration;
import com.github.hexocraft.configurate.gson.GsonConfiguration;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Configuration(
    clazz = GsonAnnotationConfig.class,
    file = "../target/tmp/config.json",
    header = "Default header from annotation"
)
@ConfigSerializable
public class GsonAnnotationConfig extends GsonConfiguration<GsonAnnotationConfig> {

    public int version = 1;

    public Database database;

    public GsonAnnotationConfig() throws ConfigurateException {
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
        public String host = "localhost";
        public int port = 3306;
    }
}