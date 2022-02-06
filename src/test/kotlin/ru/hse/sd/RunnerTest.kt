package ru.hse.sd

import org.junit.jupiter.api.Test
import ru.hse.sd.cmd.*
import ru.hse.sd.cmd.IO
import ru.hse.sd.cmd.checkStreams
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.env.cmd.BuiltinCommandEnvironment
import ru.hse.sd.env.cmd.CommandEnvironment
import ru.hse.sd.parser.Parser
import kotlin.test.assertEquals

internal class RunnerTest {
    private val cmdEnvironments = listOf<CommandEnvironment>(BuiltinCommandEnvironment().apply {
        registerCommand("exit", ExitCommand)
        registerCommand("echo", Echo)
        registerCommand("cat", Cat)
    })

    private fun test_command(statementString: String, stopCLI: Boolean, output: String, errors: String = "") {
        val parser = Parser()
        val testRunner = Runner(cmdEnvironments, VariableEnvironment())
        val testIO = IO("")
        val statement = parser.parse(statementString).first()
        assertEquals(stopCLI, testRunner.run(statement, testIO))
        testIO.checkStreams(output, errors)
    }

    @Test
    fun `test no commands`() {
        test_command(" ", false, "")
    }

    @Test
    fun `test 1 command, not stop CLI`() {
        test_command("echo 1", false, "1\n")
    }

    @Test
    fun `test 1 command, stop CLI`() {
        test_command("exit", true, "")
    }

    @Test
    fun `test 2 commands pipe, not stop CLI`() {
        test_command("echo 1 | cat", false, "1\n")
    }

    @Test
    fun `test 2 commands pipe, stop CLI after second command`() {
        test_command("echo 1 | exit", true, "")
    }

    @Test
    fun `test 2 commands pipe, stop CLI after first command`() {
        test_command("exit | echo 1", true, "")
    }
}
