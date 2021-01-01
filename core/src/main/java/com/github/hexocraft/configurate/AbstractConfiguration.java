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
import com.github.hexocraft.configurate.annotations.ConfigurationClass;
import com.github.hexocraft.configurate.annotations.ConfigurationFile;
import com.github.hexocraft.configurate.annotations.ConfigurationHeader;
import com.github.hexocraft.configurate.serialize.ColorSerializer;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.*;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;

import java.io.File;
import java.util.Objects;

@SuppressWarnings({"unchecked", "unused", "FieldCanBeLocal"})
public abstract class AbstractConfiguration<C extends AbstractConfiguration<?, ?>, N extends ScopedConfigurationNode<?>> {

    /**
     * Configuration file from which to save and/or load.
     */
    private transient File file;

    /**
     * Holder for general configuration options
     */
    private transient ConfigurationOptions options;

    /**
     * Modes which {@link ConfigurationLoader}s can use to manipulate headers
     * when loading/saving.
     * <p>
     * The {@link HeaderMode} is automatically defined depending on the {@link ConfigurationOptions}.
     * <p>
     * If the {@link ConfigurationOptions} define an header, then the {@link HeaderMode}
     * is set to {@link HeaderMode#PRESERVE}
     * Else, if the {@link AbstractConfiguration} define {@link ConfigurationHeader} then
     * the {@link HeaderMode} is set to {@link HeaderMode#PRESET}
     */
    private transient HeaderMode headerMode;

    /**
     * Object which can load and save {@link ConfigurationNode} objects in a specific
     * configuration format.
     */
    private transient ConfigurationLoader<? extends ConfigurationNode> loader;

    /**
     * A mapper that converts between configuration nodes and Java objects.
     */
    private transient ObjectMapper<C> mapper;

    /**
     * A configuration node that can have a comment attached to it.
     */
    private transient volatile N root;


    /**
     * Create an AbstractConfiguration object which can load and save.
     * <p>
     * If type is null, then the class to map will be set from {@link ConfigurationClass} annotation.
     * If file is null, then the file will be set from {@link ConfigurationFile} annotation.
     *
     * @param type       Token holding the class type to map
     * @param file       The configuration file
     * @param options    Configuration options
     * @param headerMode HeaderMode to use
     * @throws ConfigurateException if an error happen while instantiating the object
     */
    public AbstractConfiguration(@Nullable TypeToken<C> type, @Nullable File file, @Nullable ConfigurationOptions options, @Nullable HeaderMode headerMode) throws ConfigurateException {

        // If type is not defined, then try to get it from annotations
        type = type == null ? getAnnotatedClassType() : type;

        // If file is not defined, then try to get it from annotations
        file = file == null ? getAnnotatedFile() : file;

        // If options is not defined, then use default options
        options = options == null ? ConfigurationOptions.defaults() : options;
        options = options.serializers(build -> build.register(ColorSerializer.TYPE, ColorSerializer.INSTANCE));

        // If headerMode is not defined, then define a default value depending on options header
        if (headerMode == null) {
            // If options has no header defined, then try to get it from annotations.
            if (options.header() == null) {
                try {
                    options = options.header(getAnnotatedHeader());
                    headerMode = HeaderMode.PRESET;
                } catch (NullPointerException ignored) {
                    headerMode = HeaderMode.NONE;
                }
            } else {
                headerMode = HeaderMode.PRESERVE;
            }
        }

        // Finalising class creation
        this.file = file;
        this.options = options;
        this.headerMode = headerMode;
        this.loader = Objects.requireNonNull(createLoader(file, options, headerMode));
        this.mapper = ObjectMapper.factory().get(type);
        this.root = null;
    }

    /**
     * Abstract method to create configuration loader
     *
     * @param file       The configuration file
     * @param options    Configuration options
     * @param headerMode HeaderMode to use
     * @return ConfigurationLoader
     */
    protected abstract ConfigurationLoader<? extends ConfigurationNode> createLoader(File file, ConfigurationOptions options, HeaderMode headerMode);

    /**
     * Gets the token holding the class type to map from annotation
     *
     * @return token
     * @throws ConfigurateException if {@link ConfigurationClass} annotation is not defined
     */
    private TypeToken<C> getAnnotatedClassType() throws ConfigurateException {
        try {
            return (TypeToken<C>) TypeToken.get(this.getClass().getAnnotation(ConfigurationClass.class).value());
        } catch (NullPointerException ignored) {
            try {
                return (TypeToken<C>) TypeToken.get(this.getClass().getAnnotation(Configuration.class).clazz());
            } catch (NullPointerException e) {
                throw new ConfigurateException("Configuration class is not defined", e);
            }
        }
    }

    /**
     * Gets file to load and/or save from annotation
     *
     * @return File
     * @throws ConfigurateException if {@link ConfigurationFile} annotation is not defined
     */
    private File getAnnotatedFile() throws ConfigurateException {
        try {
            return new File(this.getClass().getAnnotation(ConfigurationFile.class).value());
        } catch (NullPointerException e) {
            try {
                return new File(this.getClass().getAnnotation(Configuration.class).file());
            } catch (NullPointerException ignored) {
                return null;
            }
        }
    }

    /**
     * Gets header comment from annotation
     *
     * @return Header comment
     * @throws ConfigurateException if {@link ConfigurationHeader} annotation is not defined
     */
    private String getAnnotatedHeader() throws ConfigurateException {
        try {
            return this.getClass().getAnnotation(ConfigurationHeader.class).value();
        } catch (NullPointerException e) {
            try {
                return this.getClass().getAnnotation(Configuration.class).header();
            } catch (NullPointerException ignored) {
                return "";
            }
        }
    }

    /**
     * Load the configuration from file and populate the {@link AbstractConfiguration} object.
     *
     * @return Configuration instance
     */
    public C load() throws ConfigurateException {
        if(file != null) {
            return loadFrom(file);
        }
        return null;
    }

    /**
     * Load the configuration from file and populate the {@link AbstractConfiguration} object.
     *
     * @return Configuration instance
     */
    public C loadFrom(@NonNull File file) throws ConfigurateException {
        try {
            // Update default file
            if (this.file == null) {
                this.file = file;
                this.loader = createLoader(file, options, headerMode);
            }

            // Update the root node
            root = (N) loader.load(options);

            if(mapper.canCreateInstances()) {
                // Populate the Configuration object
                AbstractConfiguration<C, N> instance = (AbstractConfiguration<C, N>) mapper.load(root);

                // Update instance
                instance.file = this.file;
                instance.options = this.options;
                instance.headerMode = this.headerMode;
                instance.loader = this.loader;
                instance.mapper = this.mapper;
                instance.root = this.root;

                return (C) instance;
            }

            return null;
        } catch (ConfigurateException e) {
            throw new ConfigurateException("Unable to save file: " + file.getName(), e);
        }
    }

    /**
     * Load the configuration from the default file or the configuration file and populate the {@link AbstractConfiguration} object.
     *
     * @return Configuration instance
     */
    public C loadDefault(@NonNull File defaultFile) throws ConfigurateException {
        // Load from configuration file first
        if (file != null) {
            return loadFrom(file);
        } else if (defaultFile.exists()) {
            return loadFrom(defaultFile);
        } else {
            throw new ConfigurateException("An error occurred while loading default configuration file");
        }
    }

    /**
     * Save the the {@link AbstractConfiguration} object to file.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void save() throws ConfigurateException {
        try {
            // Make sure parent folder exist
            File parent = file.getParentFile();
            parent.mkdirs();

            //
            if(root == null) {
                root = (N) loader.load(options);
            }

            // Apply changes
            mapper.save((C) this, root);

            // Save the file
            loader.save(root);

        } catch (ConfigurateException | ClassCastException e) {
            throw new ConfigurateException("Unable to save file: " + file.getName(), e);
        }
    }

    public File getFile() {
        return file;
    }

    public N getRoot() {
        return root;
    }
}
