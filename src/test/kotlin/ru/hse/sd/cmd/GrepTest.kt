package ru.hse.sd.cmd

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.hse.sd.TmpFile

internal class GrepTest {
    private fun testSuccessGrepCall(inputString: String, args: List<String>, outputString: String) {
        val io = IO(inputString)
        val cmdResult = Grep.execute(emptyMap(), args, io)
        Assertions.assertEquals(ReturnCode.success, cmdResult)
        io.checkStreams(outputString)
    }

    @Test
    fun `test with input file`() {
        TmpFile().use { file ->
            file.file.writeBytes("- Hello World\n- Hello word\n".toByteArray())
            val io = IO("")
            val cmdResult = Grep.execute(emptyMap(), listOf("World", file.file.path), io)
            Assertions.assertEquals(ReturnCode.success, cmdResult)
            io.checkStreams("- Hello World\n")
        }
    }

    @Test
    fun `test with input from io`() {
        val io = IO("- Hello World\n- Hello word\n")
        val cmdResult = Grep.execute(emptyMap(), listOf("World"), io)
        Assertions.assertEquals(ReturnCode.success, cmdResult)
        io.checkStreams("- Hello World\n")
    }

    @Test
    fun `test without -w`() {
        val inputLine = "HelloRealWorld"
        testSuccessGrepCall(inputLine, listOf("Real"), "$inputLine\n")
    }

    @Test
    fun `test -w option line with match not separated by constituent characters`() {
        val inputLine = "HelloRealWorld"
        testSuccessGrepCall(inputLine, listOf("-w", "Real"), "")
    }

    @Test
    fun `test -w option line with match separated by constituent characters`() {
        val inputLine = "Hello Real!"
        testSuccessGrepCall(inputLine, listOf("-w", "Real"), "$inputLine\n")
        testSuccessGrepCall(inputLine, listOf("-w", "Real!"), "$inputLine\n")
        testSuccessGrepCall(inputLine, listOf("-w", "Hello"), "$inputLine\n")
        testSuccessGrepCall(inputLine, listOf("-w", inputLine), "$inputLine\n")
    }

    @Test
    fun `test -w option line with match separated and not separated by constituent characters`() {
        val inputLine = "Hello RealReal Real"
        testSuccessGrepCall(inputLine, listOf("-w", "Real"), "$inputLine\n")
    }

    @Test
    fun `test -w option by pattern without non-word constituent characters`() {
        val inputLine = "Hello Real"
        testSuccessGrepCall(inputLine, listOf("-w", "Real"), "$inputLine\n")
    }

    @Test
    fun `test -w option by pattern with non-word constituent characters`() {
        val inputLine = "Hello Real Real"
        testSuccessGrepCall(inputLine, listOf("-w", "Real Real"), "$inputLine\n")
    }

    @Test
    fun `test -i option`() {
        val inputLine = "Hello Real"
        testSuccessGrepCall(inputLine, listOf("-w", "real"), "$inputLine\n")
    }

    @Test
    fun `test -A option without intersection`() {
        val inputLines = "a\nb\nc\na\nb\nc"
        testSuccessGrepCall(inputLines, listOf("-A", "1", "a"), "a\nb\na\nb\n")
    }

    @Test
    fun `test -A option with intersection`() {
        val inputLines = "a\nb\nc\na\nb\nc"
        testSuccessGrepCall(inputLines, listOf("-A", "3", "a"), "$inputLines\n")
    }
}
