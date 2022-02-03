package ru.hse.sd.cmd

import org.junit.jupiter.api.Test
import kotlin.test.assertSame

class EchoTest {
    @Test
    fun `test not empty input stream`() {
        val testIO = IO("some input")
        val cmdRes = Echo.eval(mapOf(), listOf(), testIO)
        testIO.checkStreams("", "")
        assertSame(ReturnCode.success, cmdRes)
    }

    @Test
    fun `test 0 arguments`() {
        val testIO = IO("")
        val cmdRes = Echo.eval(mapOf(), listOf(), testIO)
        testIO.checkStreams("", "")
        assertSame(ReturnCode.success, cmdRes)
    }

    @Test
    fun `test 1 argument`() {
        val testIO = IO("")
        val cmdRes = Echo.eval(mapOf(), listOf("arg1"), testIO)
        testIO.checkStreams("arg1", "")
        assertSame(ReturnCode.success, cmdRes)
    }

    @Test
    fun `test 2 arguments`() {
        val testIO = IO("")
        val cmdRes = Echo.eval(mapOf(), listOf("arg1", "arg2"), testIO)
        testIO.checkStreams("arg1 arg2", "")
        assertSame(ReturnCode.success, cmdRes)
    }
}
