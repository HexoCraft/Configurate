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
package com.github.hexocraft.configuration.serialize;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.awt.*;
import java.lang.reflect.Type;

public final class ColorSerializer implements TypeSerializer<Color> {

    public static final ColorSerializer INSTANCE = new ColorSerializer();
    public static final Class<Color> TYPE = Color.class;

    @Override
    public Color deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
        String color = node.getString().replaceAll("#", "").replaceAll("0x", "").replaceAll("_", "");
        if (color.length() == 6)
            return new Color((int) Long.parseLong(color, 16), false);
        else if (color.length() == 8)
            return new Color((int) Long.parseLong(color, 16), true);
        return null;
    }

    @Override
    public void serialize(final Type type, final @Nullable Color obj, final ConfigurationNode node) throws SerializationException {
        String alpha = Integer.toHexString(obj.getAlpha());
        String red = Integer.toHexString(obj.getRed());
        String green = Integer.toHexString(obj.getGreen());
        String blue = Integer.toHexString(obj.getBlue());
        alpha = alpha.length() == 1 ? "0" + alpha : alpha;
        red = red.length() == 1 ? "0" + red : red;
        green = green.length() == 1 ? "0" + green : green;
        blue = blue.length() == 1 ? "0" + blue : blue;
        String argb = "#" + alpha.toUpperCase() + red.toUpperCase() + green.toUpperCase() + blue.toUpperCase();
        node.raw(argb);
    }
}
