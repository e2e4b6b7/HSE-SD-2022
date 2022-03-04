package ru.hse.sd.cmd

import org.junit.jupiter.api.Test
import ru.hse.sd.env.VariableEnvironment
import java.nio.file.Path
import kotlin.test.assertSame

class PwdTest {

    @Test
    fun `test simple pwd`() {
        val testIO = IO("some input")
        val cmdRes = Pwd.execute(VariableEnvironment(), listOf(), testIO)
        testIO.checkStreams(Path.of("").toAbsolutePath().toString() + "\n", "")
        assertSame(ReturnCode.success, cmdRes)
    }
}
