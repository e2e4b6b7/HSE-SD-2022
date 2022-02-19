package ru.hse.sd

import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.env.cmd.CommandEnvironment
import ru.hse.sd.parser.Parser

/** Command-line interpreter */
class SShell(
    /** Environments of commands, where calling commands are searched */
    cmdEnv: List<CommandEnvironment>,
    /** Contains all environment variables */
    varEnv: VariableEnvironment,
    /** Class, which interacts with user */
    private val userInteraction: UserInteraction,
    /** Query parser */
    private val parser: Parser,
    /** Container of input, output and other streams */
    private val io: IO,
) {
    private val runner = Runner(cmdEnv, varEnv)

    /**
     * Starts interpreter.
     * Reads statements, parse them and call.
     */
    fun start() {
        while (true) {
            val input = userInteraction.read() ?: break
            val program = parser.parse(input)
            for (statement in program) {
                val result = runner.run(statement, io)
                if (result) return
            }
        }
    }
}
