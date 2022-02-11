package ru.hse.sd

import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.env.cmd.CommandEnvironment
import ru.hse.sd.parser.Parser

/** Command-line interpreter */
class SShell(
    /** Environments of commands, where calling commands are searched */
    cmdEnv: List<CommandEnvironment>,
    /** Contains all environment variables */
    private val varEnv: VariableEnvironment,
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
            val program = parser.parse(userInteraction.read())
            for (preStatement in program) {
                val statement = parser.subst(preStatement, varEnv.mapView)
                val result = runner.run(statement, io)
                if (result) return
            }
        }
    }
}
