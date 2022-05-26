package ru.hse.sd.cmd

import org.junit.jupiter.api.Test
import ru.hse.sd.env.VariableEnvironment
import kotlin.test.assertSame

internal class EchoTest {
    @Test
    fun `test not empty input stream`() {
        val testIO = IO("some input")
        val cmdRes = Echo.execute(VariableEnvironment(), listOf(), testIO)
        testIO.checkStreams("\n", "")
        assertSame(ReturnCode.success, cmdRes)
    }

    @Test
    fun `test 0 arguments`() {
        val testIO = IO("")
        val cmdRes = Echo.execute(VariableEnvironment(), listOf(), testIO)
        testIO.checkStreams("\n", "")
        assertSame(ReturnCode.success, cmdRes)
    }

    @Test
    fun `test 1 argument`() {
        val testIO = IO("")
        val cmdRes = Echo.execute(VariableEnvironment(), listOf("arg1"), testIO)
        testIO.checkStreams("arg1\n", "")
        assertSame(ReturnCode.success, cmdRes)
    }

    @Test
    fun `test 2 arguments`() {
        val testIO = IO("")
        val cmdRes = Echo.execute(VariableEnvironment(), listOf("arg1", "arg2"), testIO)
        testIO.checkStreams("arg1 arg2\n", "")
        assertSame(ReturnCode.success, cmdRes)
    }
}
