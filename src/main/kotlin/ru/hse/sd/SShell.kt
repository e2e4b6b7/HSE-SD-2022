package ru.hse.sd

import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.env.cmd.CommandEnvironment
import ru.hse.sd.parser.Parser

class SShell(
    private val cmdEnv: List<CommandEnvironment>,
    private val varEnv: VariableEnvironment,
    private val userInteraction: UserInteraction,
    private val parser: Parser,
    private val io: IO,
) {
    private val runner = Runner(cmdEnv, varEnv)

    fun start() {
        while (true) {
            val program = parser.parse(userInteraction.read())
            for (statement in program) {
                val result = runner.run(statement, io)
                if (result) return
            }
        }
    }
}
