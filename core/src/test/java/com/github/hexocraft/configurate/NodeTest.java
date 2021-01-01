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


import io.leangen.geantyref.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.BasicConfigurationNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeTest {

    @Test
    public void UndefNodeTest() {
        Assertions.assertDoesNotThrow(() -> {
            BasicConfigurationNode root = BasicConfigurationNode.root();

            assertEquals(true, root.node("boolean").getBoolean(true));
            assertEquals(1, root.node("int").getLong(1));
            assertEquals(1L, root.node("long").getLong(1L));
            assertEquals(2.3f, root.node("float").getFloat(2.3f));
            assertEquals(2.3d, root.node("double").getDouble(2.3d));
            assertEquals("string", root.node("string").getString("string"));
        });
    }

    @Test
    public void SimpleNodeTest() {
        Assertions.assertDoesNotThrow(() -> {
            BasicConfigurationNode root = BasicConfigurationNode.root();

            root.node("debug").set(false);
            assertEquals(false, root.node("debug").getBoolean());
        });
    }

    @Test
    public void MultiLevelNodeTest() {
        Assertions.assertDoesNotThrow(() -> {
            BasicConfigurationNode root = BasicConfigurationNode.root();

            root.node("db").node("host").set("localhost");
            root.node("db").node("port").set(3306);

            assertEquals("localhost", root.node("db").node("host").getString());
            assertEquals("localhost", root.node("db", "host").getString());
            assertEquals("localhost", root.node((Object[]) "db.host".split("\\.")).getString());

            assertEquals(3306, root.node("db").node("port").getInt());
            assertEquals(3306, root.node("db", "port").getInt());
            assertEquals(3306, root.node((Object[]) "db.port".split("\\.")).getInt());
        });
    }

    @Test
    public void ParentNodeTest() {
        Assertions.assertDoesNotThrow(() -> {
            BasicConfigurationNode db = BasicConfigurationNode.root();
            db.node("db").node("host").set("localhost");
            db.node("db").node("port").set(3306);

            BasicConfigurationNode root = BasicConfigurationNode.root();
            root.node("root").set(db);

            assertEquals("localhost", root.node("root").node("db").node("host").getString());
            assertEquals("localhost", root.node("root", "db", "host").getString());
            assertEquals("localhost", root.node((Object[]) "root.db.host".split("\\.")).getString());

            assertEquals(3306, root.node("root").node("db").node("port").getInt());
            assertEquals(3306, root.node("root", "db", "port").getInt());
            assertEquals(3306, root.node((Object[]) "root.db.port".split("\\.")).getInt());
        });
    }

    @Test
    public void TypeTokenNodeTest() {
        Assertions.assertDoesNotThrow(() -> {
            BasicConfigurationNode root = BasicConfigurationNode.root();

            root.node("db").node("host").set("localhost");
            root.node("db").node("port").set(3306);

            assertEquals("localhost", root.node("db").node("host").get(TypeToken.get(String.class)));
            assertEquals(3306, root.node("db").node("port").get(TypeToken.get(int.class)));
        });
    }
}