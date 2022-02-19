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
    private val cmdEnvironments = listOf<CommandEnvironment>(
        BuiltinCommandEnvironment().apply {
            registerCommand("exit", ExitCommand)
            registerCommand("echo", Echo)
            registerCommand("cat", Cat)
        }
    )

    private fun test_statement(statementString: String, stopCLI: Boolean, output: String, errors: String = "",
                               varEnv: VariableEnvironment? = null) {
        val parser = Parser()
        val testVarEnv = varEnv ?: VariableEnvironment()
        val testRunner = Runner(cmdEnvironments, testVarEnv)
        val testIO = IO("")
        val statement = parser.subst(parser.parse(statementString)!!.first(), testVarEnv.mapView)
        assertEquals(stopCLI, testRunner.run(statement, testIO))
        testIO.checkStreams(output, errors)
    }

    @Test
    fun `test no commands`() {
        test_statement(" ", false, "")
    }

    @Test
    fun `test 1 command, not stop CLI`() {
        test_statement("echo 1", false, "1\n")
    }

    @Test
    fun `test 1 command, stop CLI`() {
        test_statement("exit", true, "")
    }

    @Test
    fun `test subst, using variable from built variable environment`() {
        val varEnv = VariableEnvironment().apply { set("x", "1") }
        test_statement("echo \$x", false, "1\n","", varEnv)
    }

    @Test
    fun `test weak quoting`() {
        test_statement("echo '\$x'", false, "\$x\n")
    }

    @Test
    fun `test full quoting`() {
        val varEnv = VariableEnvironment().apply { set("x", "1") }
        test_statement("echo \"\$x\"", false, "1\n","", varEnv)
    }

    @Test
    fun `test run few statements with one runner`() {
        test_statement("echo 1", false, "1\n",)
        test_statement("exit", true, "")
    }

    @Test
    fun `test run few statements with one runner, including assignment `() {
        val varEnv = VariableEnvironment()
        test_statement("x=1", false, "", "", varEnv)
        test_statement("echo \$x", false, "1\n", "", varEnv)
    }

    @Test
    fun `test 2 commands pipe, not stop CLI`() {
        test_statement("echo 1 | cat", false, "1\n")
    }

    @Test
    fun `test 2 commands pipe, stop CLI after second command`() {
        test_statement("echo 1 | exit", true, "")
    }

    @Test
    fun `test 2 commands pipe, stop CLI after first command`() {
        test_statement("exit | echo 1", true, "")
    }
}
