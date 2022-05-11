package ru.hse.sd.cmd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class GrepArgumentsTest {
    @Suppress("UNCHECKED_CAST")
    private fun parse(vararg args: String) = parse(args as Array<String>)

    @Test
    fun `test grep arguments simple`() {
        assertEquals(
            parse(".*", "File.txt"),
            GrepArguments(
                pattern = ".*",
                filePath = "File.txt",
                wholeWord = false,
                ignoreCase = false,
                numOfLinesAfterMatched = 0
            )
        )
    }

    @Test
    fun `test grep arguments flags`() {
        assertEquals(
            parse("-i", ".*", "-w", "File.txt", "-A", "10"),
            GrepArguments(
                pattern = ".*",
                filePath = "File.txt",
                wholeWord = true,
                ignoreCase = true,
                numOfLinesAfterMatched = 10
            )
        )
    }
}
