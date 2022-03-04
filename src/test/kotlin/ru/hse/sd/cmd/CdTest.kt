package ru.hse.sd.cmd

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.hse.sd.env.VariableEnvironment
import java.nio.file.Path

class CdTest {

    @Test
    fun `test cd with 0 arguments`() {
        val testIO = IO("some input")
        val cmdRes = Cd.execute(VariableEnvironment(), emptyList(), testIO)
        testIO.checkStreams(Path.of("").toAbsolutePath().toString() + "\n", "")
        Assertions.assertEquals(ReturnCode.success, cmdRes)
    }

    @Test
    fun `test simple cd`() {
        val testIO = IO("some input")
        val cmdRes = Cd.execute(VariableEnvironment(), listOf("src"), testIO)
        val path = Path.of("src")
        testIO.checkStreams(Path.of("").toAbsolutePath().resolve(path).normalize().toString() + "\n", "")
        Assertions.assertEquals(ReturnCode.success, cmdRes)
    }

    @Test
    fun `test cd with many arguments`() {
        val testIO = IO("some input")
        val cmdRes = Cd.execute(VariableEnvironment(), listOf("src", "file"), testIO)
        Assertions.assertEquals(ReturnCode(1), cmdRes)
        testIO.checkStreams("", "Too many arguments\n")
    }

    @Test
    fun `test cd with a non-existent file`() {
        val testIO = IO("some input")
        val cmdRes = Cd.execute(VariableEnvironment(), listOf("file"), testIO)
        Assertions.assertEquals(ReturnCode(1), cmdRes)
        testIO.checkStreams("", "No such file file\n")
    }

    @Test
    fun `test cd`() {
        val testIO = IO("some input")
        val cmdRes = Cd.execute(VariableEnvironment(), listOf("../../.."), testIO)
        val path = Path.of("../../..")
        testIO.checkStreams(Path.of("").toAbsolutePath().resolve(path).normalize().toString() + "\n", "")
        Assertions.assertEquals(ReturnCode.success, cmdRes)
    }

}
