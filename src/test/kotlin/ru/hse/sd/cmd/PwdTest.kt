package ru.hse.sd.cmd

import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.test.assertSame

class PwdTest {

    @Test
    fun `test simple pwd`() {
        val testIO = IO("some input")
        val cmdRes = Pwd.execute(mapOf(), listOf(), testIO)
        testIO.checkStreams(Path.of("").toAbsolutePath().toString() + "\n", "")
        assertSame(ReturnCode.success, cmdRes)
    }

}
