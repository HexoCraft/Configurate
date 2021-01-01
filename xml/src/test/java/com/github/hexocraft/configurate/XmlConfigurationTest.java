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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


class XmlConfigurationTest {

    public static File output = Paths.get("..", "target", "tmp", "config.xml").toFile();
    public static File outputCopy = Paths.get("..", "target", "tmp", "configCopy.xml").toFile();

    @BeforeAll
    public static void init() throws IOException {
        Files.deleteIfExists(output.toPath());
    }

    @Test
    public void SaveConfigTest() {
        Assertions.assertDoesNotThrow(() -> {
            // Delete config file
            Files.deleteIfExists(output.toPath());

            // Create HoconConfig and save it
            XmlBaseConfig config = new XmlBaseConfig(output);
            config.save();

            // Output file must exist
            Assertions.assertTrue(output.exists());
        });
    }

    @Test
    public void LoadConfigTest() {
        Assertions.assertDoesNotThrow(() -> {
            // Delete config file
            Files.deleteIfExists(output.toPath());

            // Create HoconConfig and save it
            XmlBaseConfig config = new XmlBaseConfig(output).load();

            // Output file must exist
            Assertions.assertFalse(output.exists());
        });
    }

    @Test
    public void LoadFromConfigTest() {
        Assertions.assertDoesNotThrow(() -> {
            // Delete config file
            Files.deleteIfExists(output.toPath());

            // Create HoconConfig and save it
            XmlBaseConfig config = new XmlBaseConfig().loadFrom(output);
            config.save();

            // Output file must exist
            Assertions.assertTrue(output.exists());
        });
    }

    @Test
    public void ConfigWithFileTest() {
        Assertions.assertDoesNotThrow(() -> {
            // Delete config file
            Files.deleteIfExists(output.toPath());
            Files.deleteIfExists(outputCopy.toPath());

            // Create HoconConfig and save it
            XmlBaseConfig config = new XmlBaseConfig(output);
            config.save();

            // Output file must exist
            Assertions.assertTrue(output.exists());

            // Default values must be set
            Assertions.assertEquals(config.version, 1);
            Assertions.assertEquals(config.color, new Color(13, 32, 64));
            Assertions.assertEquals(config.aplhaColor, new Color(13, 32, 64, 128));
            Assertions.assertEquals(config.getDatabase().host, "localhost");
            Assertions.assertEquals(config.getDatabase().port, 3306);

            // Update config and save it
            config.getDatabase().host = "127.0.0.1";
            config.save();

            // Default values must be overwritten
            Assertions.assertEquals(config.getDatabase().host, "127.0.0.1");

            // Reload config from file
            config = new XmlBaseConfig(output).load();
            config.save();

            // Make sure previous changes have been applied
            Assertions.assertEquals(config.color, new Color(13, 32, 64));
            Assertions.assertEquals(config.aplhaColor, new Color(13, 32, 64, 128));
            Assertions.assertEquals(config.getDatabase().host, "127.0.0.1");

            // Create a copy
            XmlBaseConfig configCopy = new XmlBaseConfig(outputCopy).loadFrom(output);
            configCopy.save();

            // Output file must exist
            Assertions.assertTrue(outputCopy.exists());
        });
    }

    @Test
    public void ConfigWithAnnotationTest() {
        Assertions.assertDoesNotThrow(() -> {
            // Delete config file
            Files.deleteIfExists(output.toPath());

            // Create HoconAnnotationConfig and save it
            XmlAnnotationConfig config = new XmlAnnotationConfig();
            config.save();

            // Output file must exist
            Assertions.assertTrue(output.exists());

            // Default values must be set
            Assertions.assertEquals(config.version, 1);
            Assertions.assertEquals(config.getDatabase().host, "localhost");
            Assertions.assertEquals(config.getDatabase().port, 3306);

            // Update config and save it
            config.getDatabase().host = "127.0.0.1";
            config.save();

            // Default values must be overwritten
            Assertions.assertEquals(config.getDatabase().host, "127.0.0.1");

            // Reload config from file
            config = new XmlAnnotationConfig().load();

            // Make sure previous changes have been applied
            Assertions.assertEquals(config.getDatabase().host, "127.0.0.1");
        });
    }

}