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

package com.github.hexocraft.configurate.hocon;

import com.github.hexocraft.configurate.AbstractConfiguration;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;

import java.io.File;

public abstract class HoconConfiguration<C extends AbstractConfiguration<?, ?>> extends AbstractConfiguration<C, CommentedConfigurationNode> {


    /**
     * Create an HoconConfiguration object which can load and save.
     *
     * @param type       Token holding the class type to map
     * @param file       The configuration file
     * @param options    Configuration options
     * @param headerMode HeaderMode to use
     * @throws ConfigurateException if an error happen while instantiating the object
     */
    public HoconConfiguration(@Nullable TypeToken<C> type, @Nullable File file, @Nullable ConfigurationOptions options, @Nullable HeaderMode headerMode) throws ConfigurateException {
        super(type, file, options, headerMode);
    }

    /**
     * Create an HoconConfiguration object which can load and save.
     *
     * @param type       Token holding the class type to map
     * @param file       The configuration file
     * @param headerMode HeaderMode to use
     * @throws ConfigurateException if an error happen while instantiating the object
     */
    public HoconConfiguration(@Nullable TypeToken<C> type, @Nullable File file, @Nullable HeaderMode headerMode) throws ConfigurateException {
        this(type, file, null, headerMode);
    }

    /**
     * Create an HoconConfiguration object which can load and save.
     *
     * @param type Token holding the class type to map
     * @param file The configuration file
     * @throws ConfigurateException if an error happen while instantiating the object
     */
    public HoconConfiguration(@Nullable TypeToken<C> type, @Nullable File file) throws ConfigurateException {
        this(type, file, null);
    }


    /**
     * Create an HoconConfiguration object which can load and save.
     *
     * @param type Token holding the class type to map
     * @throws ConfigurateException if an error happen while instantiating the object
     */
    public HoconConfiguration(@Nullable TypeToken<C> type) throws ConfigurateException {
        this(type, null, null, null);
    }

    /**
     * Create an HoconConfiguration object which can load and save.
     *
     * @throws ConfigurateException if an error happen while instantiating the object
     */
    public HoconConfiguration() throws ConfigurateException {
        this(null, null, null, null);
    }

    /**
     * Create HOCON configuration loader
     *
     * @param file       The configuration file
     * @param options    Configuration options
     * @param headerMode HeaderMode to use
     * @return ConfigurationLoader
     */
    protected ConfigurationLoader<? extends ConfigurationNode> createLoader(File file, ConfigurationOptions options, HeaderMode headerMode) {
        HoconConfigurationLoader.Builder builder = HoconConfigurationLoader.builder();
        if (file != null) builder.file(file);
        if (options != null) builder.defaultOptions(options);
        if (headerMode != null) builder.headerMode(headerMode);
        return builder.build();
    }
}
