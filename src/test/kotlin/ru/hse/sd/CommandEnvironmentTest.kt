package ru.hse.sd

import org.apache.commons.lang3.RandomStringUtils
import java.nio.file.Path
import kotlin.test.*
import org.junit.jupiter.api.Test
import ru.hse.sd.env.cmd.BuiltinCommandEnvironment
import ru.hse.sd.cmd.Cat
import ru.hse.sd.cmd.ExternalProcess
import ru.hse.sd.env.cmd.ExternalCommandEnvironment
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.io.path.pathString


class CommandEnvironmentTest {

    @Test
    fun `test simple builtin command environment`() {
        val cmdEnv = BuiltinCommandEnvironment()
        assertNull(cmdEnv.getCommand("Does not exist"))

        cmdEnv.registerCommand("Cat", Cat)
        assertEquals(cmdEnv.getCommand("Cat"), Cat)

        val cmdWithArgs = ExternalProcess(Path.of("PathNotExits"))
        cmdEnv.registerCommand("New Command", cmdWithArgs)
        assertEquals(cmdEnv.getCommand("New Command"), cmdWithArgs)

        cmdEnv.registerCommand("Same Command", cmdWithArgs)
        assertEquals(cmdEnv.getCommand("Same Command"), cmdWithArgs)
    }

    @Test
    fun `test simple external command environment`() {
        val cmdEnv = ExternalCommandEnvironment(emptyList())

        assertNull(cmdEnv.getCommand("notExistCommand"))

        val directoryPath = createTempDirectory()
        val commandName = RandomStringUtils.randomAlphanumeric(16)
        val pathToFile = Path.of(
            directoryPath.pathString,
            commandName
        )
        TmpFile(pathToFile.toString()).use {
            it.file.writeBytes("Hello".toByteArray())
            cmdEnv.addPath(directoryPath.pathString)
            assertNotNull(cmdEnv.getCommand(commandName))
        }
        assertNull(cmdEnv.getCommand(commandName))
    }
}
