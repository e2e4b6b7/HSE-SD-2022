package ru.hse.sd

import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.env.cmd.CommandEnvironment
import ru.hse.sd.parser.Statement

class Runner(val cmdEnv: List<CommandEnvironment>, val varEnv: VariableEnvironment) {
    fun run(statement: Statement, io: IO) {
        throw NotImplementedError()
    }
}
