package ru.hse.sd.cmd

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.hse.sd.TmpFile
import ru.hse.sd.env.VariableEnvironment

internal class WordCountTest {
    @Test
    fun `check io input`() {
        val io = IO("Hello world!\nBye,   world.\n")
        val cmdResult = WordCount.execute(VariableEnvironment(), emptyList(), io)
        assertEquals(ReturnCode.success, cmdResult)
        io.checkStreams("\t\t3\t\t4\t\t27\n")
    }

    @Test
    fun `check file input`() {
        TmpFile().use { file ->
            file.file.writeBytes("- Hi\r\n- Hi\r\n- Are you ok?\r\n- Yes\r\n".toByteArray())
            val io = IO("Hello world!\nBye,   world.\n")
            val cmdResult = WordCount.execute(VariableEnvironment(), listOf(file.file.path), io)
            assertEquals(ReturnCode.success, cmdResult)
            io.checkStreams("\t\t5\t\t10\t\t34\n")
        }
    }
}
