package ru.hse.sd

import org.junit.jupiter.api.Test
import ru.hse.sd.env.VariableEnvironment
import java.nio.file.Path
import kotlin.test.assertEquals

internal class VariableEnvironmentTest {

    @Test
    fun `simple set-get`() {
        val env = VariableEnvironment()
        assertEquals(null, env["random"])
        env["var1"] = "value1"
        assertEquals("value1", env["var1"])
        assertEquals(null, env["var2"])
        env["var2"] = "value2"
        assertEquals("value1", env["var1"])
        assertEquals("value2", env["var2"])
        assertEquals(null, env["random"])
        env["var1"] = "another value1"
        assertEquals("another value1", env["var1"])
        assertEquals("value2", env["var2"])
    }

    @Test
    fun `simple fork`() {
        val env0 = VariableEnvironment()
        env0["var1"] = "value1"
        env0["var2"] = "value2"
        val env1 = env0.fork()
        assertEquals("value1", env1["var1"])
        assertEquals("value2", env1["var2"])
        env1["var3"] = "value3"
        assertEquals(null, env0["var3"])
        assertEquals("value3", env1["var3"])
        val env2 = env0.fork()
        assertEquals("value1", env1["var1"])
        assertEquals("value2", env1["var2"])
        assertEquals(null, env0["var3"])
        env2["var3"] = "another value3"
        env2["var4"] = "value4"
        assertEquals("another value3", env2["var3"])
        assertEquals("value4", env2["var4"])
        assertEquals("value3", env1["var3"])
        assertEquals(null, env1["var4"])
    }

    @Test
    fun `simple remove`() {
        val env = VariableEnvironment()
        env["var1"] = "value1"
        env["var2"] = "value2"
        assertEquals("value1", env["var1"])
        assertEquals("value2", env["var2"])
        env.remove("var1")
        assertEquals(null, env["var1"])
        assertEquals("value2", env["var2"])
        env["var1"] = "another value1"
        assertEquals("another value1", env["var1"])
        assertEquals("value2", env["var2"])
        env.remove("var1")
        env.remove("var2")
        assertEquals(null, env["var1"])
        assertEquals(null, env["var2"])
    }

    @Test
    fun `simple mapView`() {
        val env = VariableEnvironment()
        val map = mutableMapOf<String, String>()
        assertEquals(map, env.mapView)
        env["var1"] = "value1"
        env["var2"] = "value2"
        map["var1"] = "value1"
        map["var2"] = "value2"
        assertEquals(map, env.mapView)
        env.remove("var1")
        map.remove("var1")
        assertEquals(map, env.mapView)
    }

    @Test
    fun `get working directory`() {
        val env = VariableEnvironment()
        assertEquals(Path.of("").toAbsolutePath(), env.getWorkingDirectory())
    }

    @Test
    fun `reset working directory`() {
        val env = VariableEnvironment()
        env.changeWorkingDirectory(Path.of("src")) { a -> println(a) }
        env.resetWorkingDirectory()
        assertEquals(Path.of("").toAbsolutePath(), env.getWorkingDirectory())
    }

    @Test
    fun `change working directory`() {
        val env = VariableEnvironment()
        val path = Path.of("src")
        env.changeWorkingDirectory(path) { a -> println(a) }
        assertEquals(Path.of("").toAbsolutePath().resolve(path).normalize(), env.getWorkingDirectory())
    }
}
