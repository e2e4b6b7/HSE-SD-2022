package ru.hse.sd.cmd

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.hse.sd.TmpFile
import ru.hse.sd.env.VariableEnvironment

class CatTest {

    @Test
    fun `test cat with io argument`() {
        val io = IO("Hello world!")
        val cmdResult = Cat.execute(VariableEnvironment(), emptyList(), io)
        Assertions.assertEquals(ReturnCode.success, cmdResult)
        io.checkStreams("Hello world!")
    }

    @Test
    fun `test cat with existing file`() {
        TmpFile().use { file ->
            file.file.writeBytes("- Hello World\n- Hello word\n".toByteArray())
            val io = IO("Hello world!\nBye,   world.\n")
            val cmdResult = Cat.execute(VariableEnvironment(), listOf(file.file.path), io)
            Assertions.assertEquals(ReturnCode.success, cmdResult)
            io.checkStreams("- Hello World\n- Hello word\n")
        }
    }

    @Test
    fun `test cat with not existing file`() {
        val io = IO("Hello world!\nBye,   world.\n")
        val path = "./file-doesnt-exist"
        val cmdResult = Cat.execute(VariableEnvironment(), listOf(path), io)
        Assertions.assertEquals(ReturnCode(1), cmdResult)
        io.checkStreams("", "No such file $path\n")
    }
}
