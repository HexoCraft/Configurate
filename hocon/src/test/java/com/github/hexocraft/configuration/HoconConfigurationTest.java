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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class HoconConfigurationTest {

    public static File output = Paths.get("..", "target", "tmp", "config.conf").toFile();
    public static File outputCopy = Paths.get("..", "target", "tmp", "configCopy.conf").toFile();

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
            HoconBaseConfig config = new HoconBaseConfig(output);
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
            HoconBaseConfig config = new HoconBaseConfig(output).load();

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
            HoconBaseConfig config = new HoconBaseConfig().loadFrom(output);
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
            HoconBaseConfig config = new HoconBaseConfig(output);
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
            config = new HoconBaseConfig(output).load();

            // Make sure previous changes have been applied
            Assertions.assertEquals(config.color, new Color(13, 32, 64));
            Assertions.assertEquals(config.aplhaColor, new Color(13, 32, 64, 128));
            Assertions.assertEquals(config.getDatabase().host, "127.0.0.1");

            // Create a copy
            HoconBaseConfig configCopy = new HoconBaseConfig(outputCopy).loadFrom(output);
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
            HoconAnnotationConfig config = new HoconAnnotationConfig();
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
            config = new HoconAnnotationConfig().load();

            // Make sure previous changes have been applied
            Assertions.assertEquals(config.getDatabase().host, "127.0.0.1");
        });
    }

    @Test
    public void ConfigWithoutFileTest() {
        Assertions.assertDoesNotThrow(() -> {

            // Create HoconConfig and save it
            HoconBaseConfig config = new HoconBaseConfig();

            // Default values must be set
            Assertions.assertEquals(config.version, 1);
            Assertions.assertEquals(config.color, new Color(13, 32, 64));
            Assertions.assertEquals(config.aplhaColor, new Color(13, 32, 64, 128));
            Assertions.assertEquals(config.getDatabase().host, "localhost");
            Assertions.assertEquals(config.getDatabase().port, 3306);

            // Cannot save file
            Assertions.assertThrows(NullPointerException.class, () -> {
                config.save();
            });
        });
    }
    @Test
    public void ConfigWithFileLoadTest() {
        Assertions.assertDoesNotThrow(() -> {
            // Delete config files
            Files.deleteIfExists(output.toPath());

            // Create HoconConfig
            HoconBaseConfig config = new HoconBaseConfig(output).load();

            // Default values must be set
            Assertions.assertEquals(config.version, 1);
            Assertions.assertEquals(config.color, new Color(13, 32, 64));
            Assertions.assertEquals(config.aplhaColor, new Color(13, 32, 64, 128));
            Assertions.assertEquals(config.getDatabase().host, "localhost");
            Assertions.assertEquals(config.getDatabase().port, 3306);

            // Output file must not exist
            Assertions.assertFalse(output.exists());

            // Save config to file
            config.version = 2;
            config.save();

            // Output file must exist
            Assertions.assertTrue(output.exists());

            // Reload from file
            config = new HoconBaseConfig(output);

            // Default values must be set
            Assertions.assertEquals(config.version, 1);

            // Load from file
            config = config.load();

            // Updated version number must be loaded from file
            Assertions.assertEquals(config.version, 2);
        });
    }

}